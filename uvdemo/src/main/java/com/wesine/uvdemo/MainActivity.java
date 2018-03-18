package com.wesine.uvdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.videoupload.TXUGCPublishTypeDef;
import com.videoupload.UploadUtil;
import com.wesine.device_sdk.service.HeartBeatService;
import com.wesine.device_sdk.utils.BarcodeScanner;
import com.wesine.device_sdk.utils.async.Log;
import com.wesine.device_sdk.videouploadlib.CameraUtil;
import com.wesine.device_sdk.videouploadlib.CameraView;
import com.wesine.device_sdk.vlclib.VlcClient;
import com.wesine.device_sdk.vlclib.view.WesineGLSV;


public class MainActivity extends AppCompatActivity implements CameraUtil.OnRecordListener, UploadUtil.OnPublishResultListener {

    private static final String TAG = "MainActivity";
    private CameraView cameraView;
    private CameraUtil cameraUtil;

    private WesineGLSV player;
    private VlcClient vlcClient;
    private UploadUtil uploadUtil;
    private boolean isRecord = false;
    private String recordPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startServiceMSG();
        player = (WesineGLSV) findViewById(R.id.player);
        cameraView = (CameraView) findViewById(R.id.cameraView);
        cameraUtil = CameraUtil.getCameraUtil();
        cameraUtil.setCameraView(cameraView);
//        cameraUtil.setVideoSize(1280, 720);

        vlcClient = VlcClient.getVlcClient();
//        /*tsp://122.205.5.5:8554/1.ts*/
        vlcClient.init(this, "rtsp://admin:admin1234@192.168.1.253:554/Streaming/Channels/301");//rtsp://admin:admin1234@192.168.1.253:554/Streaming/tracks/501?starttime=20180225t013812z&endtime=20180226t013812z
        vlcClient.setWesineGLSV(player);
        vlcClient.onCreat();

        uploadUtil = UploadUtil.getUploadUtilInstance();
        uploadUtil.addResultListener(this);

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
        vlcClient.onResume();
        cameraUtil.onResume();
        cameraUtil.addOnRecordListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        vlcClient.onPause();
        cameraUtil.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        vlcClient.onDestroy();
        super.onDestroy();
        App.getRefWatcher(this).watch(this);
    }

    public void record(View view) {
        cameraUtil.record();
    }

    public void stop(View view) {
        cameraUtil.stopRecording();
    }


    public void upload(View view) {
        if (isRecord) {
            uploadUtil.init(recordPath);
            uploadUtil.beginUpload();
        } else {
            Toast.makeText(this, "record failed", Toast.LENGTH_SHORT).show();
        }
        isRecord = false;
    }

    public void pause(View view) {
        uploadUtil.pauseUpload();
    }

    public void resume(View view) {
        uploadUtil.resumeUpload();
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
        Logger.d("onProgress: " + (int) (100 * uploadBytes / totalBytes));
    }

    @Override
    public void onPublicCompele(String videoURL, String coverURL) {
        Logger.d("videoURL: %s  coverURL: %s", videoURL, coverURL);
    }
}
