package com.wesine.uvdemo;

import android.app.Application;

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
    private ZeroMQUtil zeroMQUtil;
    private Timer timer;


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
        zeroMQUtil.init("1001", "192.168.1.213", "9999");//192.168.1.207//192.168.43.15
    }


    private void heartBeat() {
        if (timer != null) {
            timer.cancel();
        }
        zeroMQUtil.getHeartPack();
        TaskExecutor.startTimerTask(new Runnable() {

            @Override
            public void run() {
                try {
                    zeroMQUtil.heartbeat();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000, 3 * 1000);

    }

    @Override
    public void onLowMemory() {
        timer.cancel();
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        timer.cancel();
        super.onTerminate();
    }

    public static App getInstance() {
        return instance;
    }
}
