package com.wesine.device_sdk.videouploadlib;

import android.util.Log;

import com.tencent.cos.xml.utils.StringUtils;
import com.videoupload.UploadUtil;
import com.wesine.device_sdk.encoder.MediaAudioEncoder;
import com.wesine.device_sdk.encoder.MediaEncoder;
import com.wesine.device_sdk.encoder.MediaMuxerWrapper;
import com.wesine.device_sdk.encoder.MediaVideoEncoder;

import java.io.IOException;

/**
 * Created by doug on 18-2-27.
 */

public class CameraUtil {
    private static final boolean DEBUG = false;    // TODO set false on release
    private static final String TAG = CameraUtil.class.getSimpleName();
    private static CameraUtil mCameraUtil;
    private UploadUtil uploadUtilInstance;

    /**
     * for camera preview display
     */
    private CameraView mCameraView;
    /**
     * muxer for audio/video recording
     */
    private MediaMuxerWrapper mMuxer;
    private OnRecordListener onRecordListener;

    private CameraUtil() {
        // need default constructor
    }

    public static CameraUtil getCameraUtil() {
        if (mCameraUtil == null) {
            synchronized (CameraUtil.class) {
                if (mCameraUtil == null) {
                    mCameraUtil = new CameraUtil();
                }
            }
        }
        return mCameraUtil;
    }

    private boolean init() {
        try {
            uploadUtilInstance = UploadUtil.getUploadUtilInstance();
            setVideoSize(640, 480);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    protected void setVideoSize(int width, int height) {
        if (width <= 0) {
            width = 640;
        }
        if (height <= 0) {
            height = 480;
        }
        mCameraView.setVideoSize(width, height);
        updateScaleModeText();
    }

    public void setCameraView(CameraView cameraView) {
        this.mCameraView = cameraView;
        init();
    }


    public void onResume() {
        if (DEBUG) Log.v(TAG, "onResume:");
        mCameraView.onResume();
    }

    public void onPause() {
        if (DEBUG) Log.v(TAG, "onPause:");
        stopRecording();
        mCameraView.onPause();
    }

    /*for scale mode display*/
    protected void changeScale() {
        final int scale_mode = (mCameraView.getScaleMode() + 1) % 4;
        mCameraView.setScaleMode(scale_mode);
        updateScaleModeText();
    }

    /*start recording*/
    public void record() {
        if (mMuxer == null) {
            startRecording();
        } else {
            stopRecording();
        }
    }


    private void updateScaleModeText() {
        final int scale_mode = mCameraView.getScaleMode();
//        mScaleModeView.setText(
//                scale_mode == 0 ? "scale to fit"
//                        : (scale_mode == 1 ? "keep aspect(viewport)"
//                        : (scale_mode == 2 ? "keep aspect(matrix)"
//                        : (scale_mode == 3 ? "keep aspect(crop center)" : ""))));
    }

    /**
     * start resorcing
     * This is a sample project and call this on UI thread to avoid being complicated
     * but basically this should be called on private thread because prepareing
     * of encoder is heavy work
     */
    private void startRecording() {
        if (DEBUG) Log.v(TAG, "startRecording:");
        try {
            mMuxer = new MediaMuxerWrapper(".mp4");    // if you record audio only, ".m4a" is also OK.
            if (true) {
                // for video capturing
                new MediaVideoEncoder(mMuxer, mMediaEncoderListener, mCameraView.getVideoWidth(), mCameraView.getVideoHeight());
            }
            if (true) {
                // for audio capturing
                new MediaAudioEncoder(mMuxer, mMediaEncoderListener);
            }
            mMuxer.prepare();
            mMuxer.startRecording();
        } catch (final IOException e) {
            Log.e(TAG, "startCapture:", e);
        }
    }

    /**
     * request stop recording
     */
    public void stopRecording() {
        if (DEBUG) Log.v(TAG, "stopRecording:mMuxer=" + mMuxer);
        String outputPath = "";
        if (mMuxer != null) {
            mMuxer.stopRecording();
            outputPath = mMuxer.getOutputPath();
            Log.d(TAG, "stopRecording: outputPath = " + outputPath);
//            onRecordListener.onRecordSuccess(outputPath);
            mMuxer = null;
            // you should not wait here
        }
        //TODO after stop recording start upload video to tencent cloud
        if (!StringUtils.isEmpty(outputPath)) {
            if (uploadUtilInstance != null) {
                uploadUtilInstance.init(outputPath);
                uploadUtilInstance.beginUpload();
            }
        }
    }

    public void addOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    interface OnRecordListener {
        void onRecordSuccess(String path);
    }

    /**
     * callback methods from encoder
     */
    private final MediaEncoder.MediaEncoderListener mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() {
        @Override
        public void onPrepared(final MediaEncoder encoder) {
            if (DEBUG) Log.v(TAG, "onPrepared:encoder=" + encoder);
            if (encoder instanceof MediaVideoEncoder)
                mCameraView.setVideoEncoder((MediaVideoEncoder) encoder);
        }

        @Override
        public void onStopped(final MediaEncoder encoder) {
            if (DEBUG) Log.v(TAG, "onStopped:encoder=" + encoder);
            if (encoder instanceof MediaVideoEncoder)
                mCameraView.setVideoEncoder(null);
        }
    };
}










