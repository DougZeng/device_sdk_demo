package com.wesine.device_sdk.vlclib;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.tencent.cos.xml.utils.StringUtils;
import com.wesine.device_sdk.utils.RtspRender;
import com.wesine.device_sdk.utils.Utils;
import com.wesine.device_sdk.vlclib.view.WesineGLSV;

/**
 * Created by doug on 18-2-27.
 */

public class VlcClient {
    private static final String TAG = VlcClient.class.getSimpleName();
    public static VlcClient vlcClient;
    private Context context;
    private WesineGLSV mWesineGLSV;
    private RtspRender mRender;
    private String url;

    private VlcClient() {

    }

    public static VlcClient getVlcClient() {
        if (vlcClient == null) {
            synchronized (VlcClient.class) {
                if (vlcClient == null) {
                    vlcClient = new VlcClient();
                }
            }
        }
        return vlcClient;
    }


    public void init(Context context, String url) {
        if (context == null) {
            return;
        }


        if (StringUtils.isEmpty(url)) {
            return;
        }
        if (!Utils.isURL(url)) {
            return;
        }
        this.context = context;
        this.url = url;

    }

    public WesineGLSV getWesineGLSV() {
        return mWesineGLSV;
    }


    public void setWesineGLSV(WesineGLSV wesineGLSV) {
        if (wesineGLSV == null) {
            throw new UnsupportedOperationException("初始化错误！");
        }
        mWesineGLSV = wesineGLSV;
    }

    public void onCreat() {
        try {

            mWesineGLSV.setEGLContextClientVersion(3);

            mRender = new RtspRender(mWesineGLSV);
            mRender.setRtspUrl(url);

            mWesineGLSV.setRenderer(mRender);
            mWesineGLSV.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    public void onResume() {
        mWesineGLSV.onResume();
    }

    public void onPause() {
        mWesineGLSV.onPause();
    }

    public void onDestroy() {
        mRender.onSurfaceDestoryed();
    }
}
