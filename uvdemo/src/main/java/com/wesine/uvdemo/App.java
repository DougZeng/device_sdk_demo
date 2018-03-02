package com.wesine.uvdemo;

import android.app.Application;


import com.litesuits.android.async.TaskExecutor;
import com.wesine.device_sdk.utils.ZeroMQUtil;

import java.util.Timer;

/**
 * Created by doug on 18-2-27.
 */

public class App extends Application {
    private static App instance;
    private Timer timer;
    private ZeroMQUtil zeroMQUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    private void init() {
        zeroMQUtil = ZeroMQUtil.getmZeroMQUtil();
        zeroMQUtil.init("1001", "tcp://192.168.43.15:9999");
        if (timer != null)
            timer.cancel();
        timer = TaskExecutor.startTimerTask(new Runnable() {

            @Override
            public void run() {
                try {
                    zeroMQUtil.heartbeat();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000, 1000 * 30);


        zeroMQUtil.init("1001", "tcp://192.168.43.15:9999");
        TaskExecutor.start(new Runnable() {
            @Override
            public void run() {
                try {
                    zeroMQUtil.sendPack("https://www.baidu.com/", "https://www.baidu.com/");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public static App getInstance() {
        return instance;
    }
}
