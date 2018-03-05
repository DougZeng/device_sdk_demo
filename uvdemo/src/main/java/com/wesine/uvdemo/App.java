package com.wesine.uvdemo;

import android.app.Application;
import android.util.Log;


import com.litesuits.android.async.TaskExecutor;
import com.wesine.device_sdk.utils.Device;
import com.wesine.device_sdk.utils.ZeroMQUtil;

import java.util.Timer;

/**
 * Created by doug on 18-2-27.
 */

public class App extends Application {
    private static final String TAG = "App";
    private static App instance;
    private Timer timer;
    private ZeroMQUtil zeroMQUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Device.init(this);
        init();
        heartBeat();
    }


    private void init() {
        zeroMQUtil = ZeroMQUtil.getmZeroMQUtil();
        zeroMQUtil.init("1001", "192.168.1.207", "9999");//192.168.1.207//192.168.43.15
    }

    public ZeroMQUtil getZeroMQUtil() {
        return zeroMQUtil;
    }

    private void heartBeat() {
        if (timer != null)
            timer.cancel();
        timer = TaskExecutor.startTimerTask(new Runnable() {

            @Override
            public void run() {
                try {
                    zeroMQUtil.heartbeat();
                    System.out.println(System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000, 1000 * 30);
    }

    public static App getInstance() {
        return instance;
    }
}
