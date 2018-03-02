package com.wesine.uvdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wesine.device_sdk.videouploadlib.CameraUtil;
import com.wesine.device_sdk.videouploadlib.CameraView;


public class MainActivity extends AppCompatActivity {

    private CameraView cameraView;
    private CameraUtil cameraUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView = (CameraView) findViewById(R.id.cameraView);
        cameraUtil = CameraUtil.getCameraUtil();
        cameraUtil.setCameraView(cameraView);
//        cameraUtil.setVideoSize(1280, 720);

    }

    @Override
    protected void onResume() {
        cameraUtil.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        cameraUtil.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void record(View view) {
        cameraUtil.record();
    }

    public void stop(View view) {
        cameraUtil.stopRecording();
    }
}
