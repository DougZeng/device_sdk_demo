package com.wesine.uvdemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.orhanobut.logger.Logger;
import com.videoupload.UploadUtil;
import com.wesine.device_sdk.service.HeartBeatService;
import com.wesine.device_sdk.videouploadlib.CameraUtil;
import com.wesine.device_sdk.videouploadlib.CameraView;
import com.wesine.device_sdk.vlclib.VlcClient;
import com.wesine.device_sdk.vlclib.view.WesineGLSV;


public class MainActivity extends AppCompatActivity implements CameraUtil.OnRecordListener, UploadUtil.OnPublishResultListener {

    private static final String TAG = "MainActivity";
    private CameraView cameraView;
    private CameraUtil cameraUtil;

    private boolean isRecord = false;
    private String recordPath;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Logger.v("cameraUtil", "onResume");
                cameraUtil.onResume();
                handler.sendEmptyMessageDelayed(2, 10000);
            } else if (msg.what == 2) {
                Logger.v("cameraUtil", "onPause");
                cameraUtil.onPause();
                handler.sendEmptyMessageDelayed(1, 10000);
            }
        }
    };
    private WesineGLSV player;
    private VlcClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
//        startServiceMSG();


        cameraView = (CameraView) findViewById(R.id.cameraView);
        cameraUtil = CameraUtil.getCameraUtil();
        cameraUtil.setCameraView(cameraView);


//        cameraUtil.setVideoSize(1280, 720);

//        uploadUtil = UploadUtil.getUploadUtilInstance();
//        uploadUtil.addResultListener(this);


//        handler.sendEmptyMessageDelayed(1, 3000);

//        player = (WesineGLSV) findViewById(R.id.player);
//        client = VlcClient.getVlcClient();
//        client.init(this,"");
//        client.setWesineGLSV(player);
//        client.onCreat();

    }



    private void startServiceMSG() {
        Intent intent = new Intent(this, HeartBeatService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("FPS-SERVER", "");
        intent.putExtras(bundle);
        startService(intent);
    }

    @Override
    protected void onResume() {

        cameraUtil.onResume();
        cameraUtil.addOnRecordListener(this);
//        client.onResume();


        super.onResume();
    }

    @Override
    protected void onPause() {

        cameraUtil.onPause();
//        client.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getRefWatcher(this).watch(this);
    }

    public void record(View view) {
        cameraUtil.record();
    }

    public void stop(View view) {
        cameraUtil.stopRecording();
    }

//
//    public void upload(View view) {
//        if (isRecord) {
//            uploadUtil.init(recordPath);
//            uploadUtil.beginUpload();
//        } else {
//            Toast.makeText(this, "record failed", Toast.LENGTH_SHORT).show();
//        }
//        isRecord = false;
//    }

    public void pause(View view) {
//        uploadUtil.pauseUpload();
    }

    public void resume(View view) {
//        uploadUtil.resumeUpload();
    }

    @Override
    public void onRecordSuccess(String path) {
        if (!TextUtils.isEmpty(path)) {
            isRecord = true;
            recordPath = path;
        }
    }


    @Override
    public void onProgress(long uploadBytes, long totalBytes) {
//        Logger.d("onProgress: " + (int) (100 * uploadBytes / totalBytes));
    }

    @Override
    public void onPublicCompele(String videoURL, String coverURL) {
//        Logger.d("videoURL: %s  coverURL: %s", videoURL, coverURL);
    }

    public void upload(View view) {
    }
}
