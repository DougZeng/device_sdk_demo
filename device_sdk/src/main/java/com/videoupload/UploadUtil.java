package com.videoupload;


import android.util.Log;

import com.litesuits.android.async.TaskExecutor;
import com.wesine.device_sdk.utils.Device;
import com.wesine.device_sdk.utils.ZeroMQUtil;

/**
 * Created by doug on 18-2-25.
 */

public class UploadUtil implements TXUGCPublishTypeDef.ITXVideoPublishListener {
    private static final String TAG = UploadUtil.class.getSimpleName();

    private TXUGCPublish mVideoPublish = null;

    private String mVideoPath;

    private String paramSignature = "";
    //    private String customKey = UploadConfig.CUSTOMKEY;
    private static UploadUtil mUploadUtil;
    private OnPublishResultListener onPublishResultListener;

    private ZeroMQUtil zeroMQUtil;


    private UploadUtil() {

    }

    public static UploadUtil getUploadUtilInstance() {
        if (mUploadUtil == null) {
            synchronized (UploadUtil.class) {
                if (mUploadUtil == null) {
                    mUploadUtil = new UploadUtil();
                }
            }
        }
        return mUploadUtil;
    }

    public void init(String videoPath) {
        this.mVideoPath = videoPath;
        System.out.println(System.currentTimeMillis());
        paramSignature = SignatureUtil.getSignatureUtil().getSignature();
        System.out.println(System.currentTimeMillis());
        zeroMQUtil = ZeroMQUtil.getmZeroMQUtil();
    }

    public void pauseUpload() {
        if (mVideoPublish != null) {
            mVideoPublish.canclePublish();
        }
    }

    public void resumeUpload() {
        if (mVideoPublish != null) {
            TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
            // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
            param.signature = paramSignature;
            param.videoPath = mVideoPath;
            int publishCode = mVideoPublish.publishVideo(param);
            if (publishCode != 0) {
                Log.e(TAG, "resumeUpload: " + "发布失败，错误码：" + publishCode);
            }
        }
    }

    public void beginUpload() {
        if (mVideoPublish == null) {
            mVideoPublish = new TXUGCPublish(Device.getApp(), UploadConfig.CUSTOMKEY);
            mVideoPublish.setListener(this);
        }

        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
        param.signature = paramSignature;
        param.videoPath = mVideoPath;
        int publishCode = mVideoPublish.publishVideo(param);
        if (publishCode != 0) {
            Log.e(TAG, "resumeUpload: " + "发布失败，错误码：" + publishCode);
        }

    }

    public void addResultListener(OnPublishResultListener onPublishResultListener) {
        this.onPublishResultListener = onPublishResultListener;
    }

    public interface OnPublishResultListener {
        void onSuccess(TXUGCPublishTypeDef.TXPublishResult result);

        void onFailed();
    }


    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {
//        Log.i(TAG, "onPublishProgress: " + (int) (100 * uploadBytes / totalBytes));
    }

    @Override
    public void onPublishComplete(final TXUGCPublishTypeDef.TXPublishResult result) {
//        Log.d(TAG, "onPublishComplete: " + result.retCode + " Msg:" + (result.retCode == 0 ? result.videoURL : result.descMsg));
        if (result.retCode == 0) {
//            zeroMQUtil.init("1001", "192.168.1.207", "9999");
//            zeroMQUtil.getURLPack(result.videoURL, result.coverURL);
//            TaskExecutor.start(new Runnable() {
//                @Override
//                public void run() {
//                    zeroMQUtil.sendPack();
//                }
//            });


//            onPublishResultListener.onSuccess(result);
        } else {
//            onPublishResultListener.onFailed();
        }
    }
}
