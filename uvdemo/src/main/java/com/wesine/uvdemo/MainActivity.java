package com.wesine.uvdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wesine.device_sdk.videouploadlib.CameraUtil;
import com.wesine.device_sdk.videouploadlib.CameraView;
import com.wesine.device_sdk.vlclib.VlcClient;
import com.wesine.device_sdk.vlclib.view.WesineGLSV;


public class MainActivity extends AppCompatActivity {

    private CameraView cameraView;
    private CameraUtil cameraUtil;

    private WesineGLSV player;
    private VlcClient vlcClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player = (WesineGLSV) findViewById(R.id.player);
        cameraView = (CameraView) findViewById(R.id.cameraView);
        cameraUtil = CameraUtil.getCameraUtil();
        cameraUtil.setCameraView(cameraView);
//        cameraUtil.setVideoSize(1280, 720);

        vlcClient = VlcClient.getVlcClient();
        /*tsp://122.205.5.5:8554/1.ts*/
        vlcClient.init(this, "rtsp://admin:admin1234@192.168.1.253:554/Streaming/tracks/501?starttime=20180225t013812z&endtime=20180226t013812zr");//rtsp://admin:admin1234@192.168.1.253:554/Streaming/tracks/501?starttime=20180225t013812z&endtime=20180226t013812z
        vlcClient.setWesineGLSV(player);
        vlcClient.onCreat();

    }

    @Override
    protected void onResume() {
        vlcClient.onResume();
        cameraUtil.onResume();
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
    }

    public void record(View view) {
        cameraUtil.record();
    }

    public void stop(View view) {
        cameraUtil.stopRecording();
    }
}
