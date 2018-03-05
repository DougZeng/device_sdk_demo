package com.wesine.device_sdk.utils;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tencent.cos.xml.utils.StringUtils;
import com.wesine.device_sdk.fps.model.Content;
import com.wesine.device_sdk.fps.model.Root;

import org.zeromq.ZMQ;

/**
 * Created by doug on 18-2-26.
 * 1 single send online
 * 2 single process receive FPS:video url,png url
 * 3 send url from tencent cloud
 */

public class ZeroMQUtil {
    private static final String TAG = "ZeroMQUtil";
    public static ZeroMQUtil mZeroMQUtil;

//    private static ZMQ.Context context = null;
//    private static ZMQ.Socket subscriber = null;

    private String mAddr = "tcp://192.168.1.194:9999";

    private String mEgID = "";

    private boolean heartbeatFlag = false;

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

    /*1 single send online*/

    /**
     * egID，收银机唯一ID，暂时由多点配置
     * "1001"
     *
     * @return
     */
    public boolean heartbeat() {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket subscriber = context.socket(ZMQ.DEALER);
        try {
            boolean b = subscriber.setIdentity(mEgID.getBytes());//FROM duodian
            System.out.println("setIdentity = " + b);
//        Log.i(TAG, "setIdentity = " + b);
            if (!b) {
                return false;
            }

            boolean connect = subscriber.connect(mAddr);// 注意，这里必须是服务器的IP地址或DNS Name
            System.out.println("connect = " + connect);
//            Log.i(TAG, "connect = " + connect);
            if (!connect) {
                return subscriber.connect(mAddr);
            }
            boolean sendMore = subscriber.sendMore("POS");//LPS SERVER
            System.out.println("sendMore = " + sendMore);
//            Log.i(TAG, "sendMore = " + sendMore);
            if (!sendMore) {
                return false;
            }
            Root root = new Root();
            root.setType("CapHeartBeat");
            root.setFrom(mEgID);
            root.setTs(TimeUtil.getUnixTimeStamp());
            String heartPack = JSON.toJSONString(root);
            boolean send = subscriber.send(heartPack);//MSG
            System.out.println("send = " + send);
//            Log.i(TAG, "send = " + send);
            if (!send) {
                return false;
            }
            heartbeatFlag = true;
//            String message = new String(subscriber.recv(0));
//            if (!StringUtils.isEmpty(message)) {
//                System.out.println(message);
//            }
            subscriber.close();
//            context.term();
//            subscriber.disconnect(mAddr);
        } catch (Exception e) {
            subscriber.close();
//            subscriber.disconnect(mAddr);
            context.term();
            e.printStackTrace();
            return false;
        }
        return true;
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

    /*3 send url from tencent cloud*/

    /**
     * @param capvideourl
     * @param cappictureurl
     * @return
     */
    public boolean sendPack(String capvideourl, String cappictureurl) {
        if (StringUtils.isEmpty(capvideourl) || StringUtils.isEmpty(cappictureurl)) {
            return false;
        }
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket subscriber = context.socket(ZMQ.DEALER);

        try {

            boolean b = subscriber.setIdentity(mEgID.getBytes());//FROM duodian
            System.out.println("setIdentity = " + b);
//        Log.i(TAG, "setIdentity = " + b);
            if (!b) {
                return false;
            }

            boolean connect = subscriber.connect(mAddr);// 注意，这里必须是服务器的IP地址或DNS Name
            System.out.println("connect = " + connect);
//            Log.i(TAG, "connect = " + connect);
            if (!connect) {
                return false;
            }
            boolean sendMore = subscriber.sendMore("LPS");//LPS SERVER
            System.out.println("sendMore = " + sendMore);
//            Log.i(TAG, "sendMore = " + sendMore);
            if (!sendMore) {
                return false;
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

            Root root = new Root();
            root.setType("CapUrlRep");
            root.setFrom(mEgID);
            root.setTs(TimeUtil.getUnixTimeStamp());
            Content content = new Content();
            content.setCapPictureUrl(cappictureurl);
            content.setCapVideoUrl(capvideourl);
            root.setContent(content);
            boolean send = subscriber.send(JSON.toJSONString(root));//MSG
            System.out.println("send = " + send);
            Log.i(TAG, "send = " + send);
            if (!send) {
                return false;
            }
//            String message = new String(subscriber.recv(0));
//            if (!StringUtils.isEmpty(message)) {
//                System.out.println(message);
//            }
            heartbeatFlag = true;
            subscriber.close();
//            subscriber.disconnect(mAddr);
//            context.term();
        } catch (Exception e) {
            subscriber.close();
//            subscriber.disconnect(mAddr);
            context.term();
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
