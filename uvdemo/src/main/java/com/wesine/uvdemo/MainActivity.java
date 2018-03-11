package com.wesine.uvdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.wesine.device_sdk.service.HeartBeatService;
import com.wesine.device_sdk.utils.BarcodeScanner;
import com.wesine.device_sdk.videouploadlib.CameraUtil;
import com.wesine.device_sdk.videouploadlib.CameraView;
import com.wesine.device_sdk.vlclib.VlcClient;
import com.wesine.device_sdk.vlclib.view.WesineGLSV;


public class MainActivity extends AppCompatActivity implements BarcodeScanner.OnScanSuccessListener {

    private static final String TAG = "MainActivity";
    private CameraView cameraView;
    private CameraUtil cameraUtil;

    private WesineGLSV player;
    private VlcClient vlcClient;
    private BarcodeScanner barcodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startServiceMSG();
        barcodeScanner = new BarcodeScanner(this);
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


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        return super.dispatchKeyEvent(event);
        barcodeScanner.analysisKeyEvent(event);
        return true;
    }

    @Override
    public void onScanSuccess(String barcode) {
        Toast.makeText(this, barcode, Toast.LENGTH_LONG).show();
    }
}
