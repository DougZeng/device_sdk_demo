package com.wesine.device_sdk.utils;


import com.wesine.device_sdk.cpyrt.CpyrtUtil;

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
            return CpyrtUtil.getCpyrtUtil().validateSerial();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
