package com.wesine.device_sdk.utils;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tencent.cos.xml.utils.StringUtils;
import com.wesine.device_sdk.fps.FPSConfig;
import com.wesine.device_sdk.fps.model.Content;
import com.wesine.device_sdk.fps.model.Root;
import com.wesine.device_sdk.utils.async.TaskExecutor;

import org.zeromq.ZMQ;

import java.util.Timer;

/**
 * Created by doug on 18-2-26.
 * 1 single send online
 * 2 single process receive FPS:video url,png url
 * 3 send url from tencent cloud
 */

public class ZeroMQUtil {
    private static final boolean DEBUG = true;    // TODO set false on release
    private static final String TAG = "ZeroMQUtil";
    public static ZeroMQUtil mZeroMQUtil;

//    private static ZMQ.Context context = null;
//    private static ZMQ.Socket subscriber = null;

    private String mAddr = "tcp://192.168.1.194:9999";

    private String mEgID = "";

    private String heartPack;

    private String urlPack;

    private Timer timer;

    private boolean heartbeatFlag = false;

    ZMQ.Context context0 = ZMQ.context(1);
    ZMQ.Context context1 = ZMQ.context(1);

    private ZeroMQUtil() {
    }

    public static ZeroMQUtil getmZeroMQUtil() {
        if (mZeroMQUtil == null) {
            synchronized (ZeroMQUtil.class) {
                if (mZeroMQUtil == null) {
                    mZeroMQUtil = new ZeroMQUtil();
                }
            }
        }
        return mZeroMQUtil;
    }

    public void init(String egID, String ip, String port) {
        if (StringUtils.isEmpty(egID)) {
            return;
        }
        if (StringUtils.isEmpty(ip)) {
            return;
        }
        if (StringUtils.isEmpty(port)) {
            return;
        }

        mAddr = String.format("tcp://%s:%s", ip, port);
        mEgID = egID;
    }

    public void getHeartPack() {
        Root root = new Root();
        root.setType(FPSConfig.CAPHEARTBEAT_TYPE);
        root.setFrom(mEgID);
        root.setTs(TimeUtil.getUnixTimeStamp());
        heartPack = JSON.toJSONString(root);
        if (DEBUG) {
            Log.i(TAG, "getHeartPack: " + heartPack);
        }
    }

    public void getHeartPack(String unixTimeStamp) {
        Root root = new Root();
        root.setType(FPSConfig.CAPHEARTBEAT_TYPE);
        root.setFrom(mEgID);
        root.setTs(unixTimeStamp);
        heartPack = JSON.toJSONString(root);
        if (DEBUG) {
            Log.i(TAG, "getHeartPack: " + heartPack);
        }
    }


    public void delayHeartBeat() {
        if (timer != null) {
            timer.cancel();
        }
        getHeartPack();
        timer = TaskExecutor.startTimerTask(new Runnable() {

            @Override
            public void run() {
                try {
                    heartbeat();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000, 1000 * 30);
    }

    /*1 single send online*/

    /**
     * egID，收银机唯一ID，暂时由多点配置
     * "1001"
     *
     * @return
     */
    public void heartbeat() {
        if (context0.isTerminated()) {
            context0 = ZMQ.context(1);
        }
        ZMQ.Socket subscriber0 = context0.socket(ZMQ.DEALER);
        try {
            boolean b = subscriber0.setIdentity(mEgID.getBytes());//FROM duodian
            if (DEBUG) {
                Log.i(TAG, "heartbeat: setIdentity " + b);
            }
            if (!b) {
                return;
            }
            boolean connect = subscriber0.connect(mAddr);// 注意，这里必须是服务器的IP地址或DNS Name
            if (!connect) {
                subscriber0.connect(mAddr);
            }
            if (DEBUG) {
                Log.i(TAG, "heartbeat: connect " + connect);
            }
            boolean sendMore = subscriber0.sendMore(FPSConfig.POS_SERVER);//LPS SERVER
            if (!sendMore) {
                return;
            }
            boolean send = subscriber0.send(heartPack);//MSG
            if (!send) {
                return;
            }
            if (DEBUG) {
                Log.i(TAG, "heartbeat: send " + send);
            }
            heartbeatFlag = true;
            String message = new String(subscriber0.recv(0));
            if (!StringUtils.isEmpty(message)) {
                System.out.println(message);
            }
            subscriber0.close();
//            subscriber.disconnect(mAddr);
            context0.term();
        } catch (Exception e) {
            subscriber0.close();
//            subscriber.disconnect(mAddr);
            context0.term();
            e.printStackTrace();
        }
    }


    /*2 single process receive FPS:video url,png url*/
    @Deprecated
    public boolean receivePack() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void sendUploadResult(String capvideourl, String cappictureurl) {
        getURLPack(capvideourl, cappictureurl);
        TaskExecutor.start(new Runnable() {
            @Override
            public void run() {
                sendPack();
            }
        });

    }

    /*3 send url from tencent cloud*/

    /**
     * @return
     */
    public boolean sendPack() {
        if (context1.isTerminated()) {
            context1 = ZMQ.context(1);
        }
        ZMQ.Socket subscriber1 = context1.socket(ZMQ.DEALER);
        try {
            boolean b = subscriber1.setIdentity(mEgID.getBytes());//FROM duodian
            if (!b) {
                return false;
            }
            if (DEBUG) {
                Log.i(TAG, "sendPack: setIdentity " + b);
            }
            boolean connect = subscriber1.connect(mAddr);// 注意，这里必须是服务器的IP地址或DNS Name
            if (!connect) {
                return false;
            }
            if (DEBUG) {
                Log.i(TAG, "sendPack: connect " + connect);
            }
            boolean sendMore = subscriber1.sendMore(FPSConfig.LPS_SERVER);//LPS SERVER
            if (!sendMore) {
                return false;
            }
            if (DEBUG) {
                Log.i(TAG, "sendPack: sendMore " + sendMore);
            }
            boolean send = subscriber1.send(urlPack);//MSG
            if (!send) {
                return false;
            }
            if (DEBUG) {
                Log.i(TAG, "sendPack: send " + send);
            }
            String message = new String(subscriber1.recv(0));
            if (!StringUtils.isEmpty(message)) {
                System.out.println(message);
            }
            heartbeatFlag = true;
            subscriber1.close();
//            subscriber.disconnect(mAddr);
            context1.term();
        } catch (Exception e) {
            subscriber1.close();
//            subscriber.disconnect(mAddr);
            context1.term();
            e.printStackTrace();
        }
        return true;
    }

    /*{
                "type": "CapUrlRep", //固定
                "from": "1001", //regID，收银机唯一ID，暂时由多点配置
                "ts": 1519559329, //Unix时间戳
                "content":{
                    "CapVideoUrl": "http://xxx.xxx.xxx...", //上传视频的url
                    "CapPictureUrl": "http://xxx.xxx.xxx..." //上传视频的封面
                    }
            }*/
    public void getURLPack(String capvideourl, String cappictureurl) {
        if (StringUtils.isEmpty(capvideourl) || StringUtils.isEmpty(cappictureurl)) {
            return;
        }
        Root root = new Root();
        root.setType(FPSConfig.CAPURLREP_TYPE);
        root.setFrom(mEgID);
        root.setTs(TimeUtil.getUnixTimeStamp());
        Content content = new Content();
        content.setCapPictureUrl(cappictureurl);
        content.setCapVideoUrl(capvideourl);
        root.setContent(content);
        urlPack = JSON.toJSONString(root);
        if (DEBUG) {
            Log.i(TAG, "getURLPack: " + urlPack);
        }
    }
}
