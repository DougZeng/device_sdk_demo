package com.wesine.uvdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;



/**
 * Created by doug on 18-2-25.
 */

public class Test extends Activity {
    private static final String TAG = Test.class.getSimpleName();
//    private static final String DIR_NAME = "videoupload";
//    private CameraView cameraView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        initView();
        initEvent();
    }

    private void initEvent() {
//        cameraView.addCameraListener(cameraListener);


    }

    private void initView() {
//        cameraView = (CameraView) findViewById(R.id.camera);
//        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);


//        CpyrtUtil cpyrtUtil = CpyrtUtil.getCpyrtUtil();
////
////        /* ---- 解析串号
////        Input：
////            byte[10+1]  key         生成串号
////        Output：
////            byte[1]     preCode     前缀，默认为'W'
////            byte[2+1]   factory     工厂，如"S1"
////            int[1]      year        年，如2018
////            int[1]      week        周，1-53
////            byte[1]     type        类型，如'A'
////            int[1]      num         当周生产序号
////        Return：
////            成功  >=0
////            失败  <0
////         ---- */
////
//
//        String serialno = "WS1WDAKS87";
//        byte[] preCode_v = new byte[1];
//        byte[] factory_v = new byte[2];
//        int[] year_v = new int[1];
//        int[] week_v = new int[1];
//        byte[] type_v = new byte[1];
//        int[] num_v = new int[1];
//        int code = cpyrtUtil.validateKey(serialno, preCode_v, factory_v, year_v, week_v, type_v, num_v);
//        if (code >= 0) {
//            tv.setText(tv.getText()
//                    + "WS_ValidateKey" + "\r\n"
//                    + "factory:" + new String(factory_v) + "\r\n"
//                    + "year:" + year_v[0] + "\r\n"
//                    + "week:" + week_v[0] + "\r\n"
//                    + "type:" + new String(type_v) + "\r\n"
//                    + "num:" + num_v[0] + "\r\n"
//            );
//        } else {
//            tv.setText("WS_ValidateKey" + " Failed " + "\r\n");
//        }
    }


    public void sendMSG(View view) {

    }


    @Override
    protected void onResume() {
        super.onResume();
//        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        cameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        cameraView.clearCameraListeners();
//        cameraView.destroy();
    }


    public void stop(View view) {
    }
}
