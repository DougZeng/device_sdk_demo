package com.videoupload;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.tencent.cos.xml.utils.StringUtils;
import com.videoupload.impl.TVCClient;
import com.videoupload.impl.TVCConstants;
import com.videoupload.impl.TVCUploadInfo;
import com.videoupload.impl.TVCUploadListener;
import com.wesine.device_sdk.utils.Device;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 短视频发布接口类
 */
public class TXUGCPublish {
    private static final String TAG = "TXVideoPublish";
    private static final long COVER_TIME = 100 * 1000;
    private Context mContext;
    private Handler mHandler;
    private TXUGCPublishTypeDef.ITXVideoPublishListener mListener;
    private boolean mPublishing;
    private TVCClient mTVCClient = null;
    private String mCustomKey;


    public TXUGCPublish(Context context, String customKey) {
        mCustomKey = customKey;
        if (context != null) {
            mContext = context;
            mHandler = new Handler(mContext.getMainLooper());
        }
    }

    public TXUGCPublish(Context context) {
        this(context, "");
    }

    public void setListener(TXUGCPublishTypeDef.ITXVideoPublishListener listener) {
        mListener = listener;
    }

    public int publishVideo(TXUGCPublishTypeDef.TXPublishParam param) {
        if (mPublishing) {
            Logger.d("there is existing publish task");
            return TVCConstants.ERR_UGC_PUBLISHING;
        }

        //TXCDRApi.txReportDAU(mContext, TXCDRDef.DR_DAU_EVENT_ID_UGC_PUBLISH);

        if (param == null) {
            Logger.d("publishVideo invalid param");
            return TVCConstants.ERR_UGC_INVALID_PARAM;
        }
        if (StringUtils.isEmpty(param.signature)) {
            Logger.d("publishVideo invalid UGCSignature");
            return TVCConstants.ERR_UGC_INVALID_SIGNATURE;
        }

        if (StringUtils.isEmpty(param.videoPath)) {
            Logger.d("publishVideo invalid videoPath");
            return TVCConstants.ERR_UGC_INVALID_VIDOPATH;
        }

        boolean bVideoFileExist = false;
        try {
            File file = new File(param.videoPath);
            bVideoFileExist = file.isFile() && file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bVideoFileExist == false) {
            //TXCLog.e(TAG, "publishVideo invalid video file");
            return TVCConstants.ERR_UGC_INVALID_VIDEO_FILE;
        }

        String coverPath = null;
        try {
            coverPath = getVideoThumb(param.videoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(param.coverPath)) {
            coverPath = param.coverPath;
            File file = new File(coverPath);
            if (!file.exists())
                return TVCConstants.ERR_UGC_INVALID_COVER_PATH;
        }

        if (mTVCClient == null) {
            mTVCClient = new TVCClient(mContext, mCustomKey, param.signature, "", param.enableResume, 10);
        } else {
            mTVCClient.updateSignature(param.signature);
        }

        TVCUploadInfo info = new TVCUploadInfo(getFileType(param.videoPath), param.videoPath, getFileType(coverPath), coverPath);
        int ret = mTVCClient.uploadVideo(info, new TVCUploadListener() {
            @Override
            public void onSucess(final String fileId, final String playUrl, final String coverUrl) {
                if (mHandler != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                TXUGCPublishTypeDef.TXPublishResult result = new TXUGCPublishTypeDef.TXPublishResult();
                                result.retCode = TXUGCPublishTypeDef.PUBLISH_RESULT_OK;
                                result.descMsg = "publish success";
                                result.videoId = fileId;
                                result.videoURL = playUrl;
                                result.coverURL = coverUrl;
                                mListener.onPublishComplete(result);
                            }
                        }
                    });
                }
                mTVCClient = null;
                mPublishing = false;
            }

            @Override
            public void onFailed(final int errCode, final String errMsg) {
                if (mHandler != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                TXUGCPublishTypeDef.TXPublishResult result = new TXUGCPublishTypeDef.TXPublishResult();
                                result.retCode = errCode;
                                result.descMsg = errMsg;
                                mListener.onPublishComplete(result);
                            }
                        }
                    });
                }
                mTVCClient = null;
                mPublishing = false;
            }

            @Override
            public void onProgress(final long currentSize, final long totalSize) {
                if (mHandler != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                mListener.onPublishProgress(currentSize, totalSize);
                            }
                        }
                    });
                }
                mPublishing = false;
            }
        });

        if (ret == TVCConstants.NO_ERROR)
            mPublishing = true;
        else
            mPublishing = false;
        return ret;
    }

    public void canclePublish() {
        if (mTVCClient != null) {
            mTVCClient.cancleUpload();
        }
        mPublishing = false;
    }

    private String getFileType(String filePath) {
        String fileType = "";
        if (filePath != null && filePath.length() != 0) {
            int index = filePath.lastIndexOf(".");
            if (index != -1) {
                fileType = filePath.substring(index + 1);
            }
        }
        return fileType;
    }


    private String getVideoThumb(String videoPath) throws IOException {
        String strCoverFilePath = null;
        FileOutputStream fOut = null;
        try {
            File videoFile = new File(videoPath);
            if (!videoFile.exists()) {
                Logger.d("record: video file is not exists when record finish");
                return null;
            }
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            File videoDir = new File(videoPath);
            media.setDataSource(videoDir.getAbsolutePath());
            Bitmap thumb = media.getFrameAtTime();//getFrameAtTime: videoFrame is a NULL pointer COVER_TIME, MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            String fileName = "";
            int index = videoPath.lastIndexOf(".");
            if (index != -1) {
                fileName = videoPath.substring(0, index);
            }
            strCoverFilePath = fileName + ".jpg";
            File f = new File(strCoverFilePath);
            if (f.exists()) {
                f.delete();
            }
            fOut = new FileOutputStream(f);
            thumb.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
            thumb.recycle();
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            fOut.close();
            e.printStackTrace();
        }
        return strCoverFilePath;
    }
}