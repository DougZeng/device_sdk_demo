package com.wesine.device_sdk.fps;

import com.litesuits.android.async.TaskExecutor;
import com.wesine.device_sdk.utils.ZeroMQUtil;

/**
 * Created by doug on 18-2-28.
 */

public class ZeroMQUtilTest {
    public static void main(String[] args) {
        final ZeroMQUtil zeroMQUtil = ZeroMQUtil.getmZeroMQUtil();
        zeroMQUtil.init("1001", "192.168.1.207", "9999");

        TaskExecutor.startTimerTask(new Runnable() {
            @Override
            public void run() {
                zeroMQUtil.heartbeat();
            }
        }, 3000, 1000 * 60 * 10);

        TaskExecutor.start(new Runnable() {
            @Override
            public void run() {
                zeroMQUtil.sendPack("https://www.baidu.com/", "https://www.baidu.com/");
            }
        });
    }
}

