package com.wesine.device_sdk.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.wesine.device_sdk.utils.FileUtils;
import com.wesine.device_sdk.utils.TimeUtil;
import com.wesine.device_sdk.utils.ZeroMQUtil;
import com.wesine.device_sdk.utils.async.TaskExecutor;

import java.io.File;
import java.util.Date;


/**
 * Created by doug on 18-3-9.
 */

public class HeartBeatService extends Service {
    private ZeroMQUtil zeroMQUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        zeroMQUtil = ZeroMQUtil.getmZeroMQUtil();
//        zeroMQUtil.init("1001", "192.168.1.213", "9999");//192.168.1.207//192.168.43.15
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        TaskExecutor.start(new Runnable() {
            @Override
            public void run() {
                try {
                    zeroMQUtil.getHeartPack(TimeUtil.getUnixTimeStamp());
                    zeroMQUtil.heartbeat();
                    //delete 7 day before file
                    FileUtils.deleteVideoFile(-7);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }

            }
        });
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 3 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }


}
