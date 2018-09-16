package com.wesine.device_sdk.utils;


import android.os.Build;

import com.wesine.device_sdk.utils.async.Log;

import leo.wesine.wesinecpyrtkey.CpyrtUtil;

/**
 * Created by doug on 18-3-1.
 */

public final class DeviceUtil {
    private static final String TAG = DeviceUtil.class.getSimpleName();

    private DeviceUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isWesineDevice() throws RuntimeException {
        try {
            String serial = Build.SERIAL;
            String serialno = serial.replace(" ", "").toUpperCase();
            if (serialno.length() > 10) {
                serialno = serialno.substring(0, 10);
            }
            Log.e("serial", serialno);
            return CpyrtUtil.getCpyrtUtil().validateSerial(serialno, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
