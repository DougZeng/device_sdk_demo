package com.wesine.device_sdk.utils;


import android.os.Build;

import leo.wesine.wesinecpyrtkey.CpyrtUtil;

/**
 * Created by doug on 18-3-1.
 */

public final class DeviceUtil {
    private static final String TAG = DeviceUtil.class.getSimpleName();

    private DeviceUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isWesineDevice() {
        try {
            String serial = Build.SERIAL;
            String serialno = serial.replace(" ", "").toUpperCase();
            return CpyrtUtil.getCpyrtUtil().validateSerial(serialno);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
