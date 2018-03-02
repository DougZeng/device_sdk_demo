package com.wesine.device_sdk.utils;

import android.content.Context;

/**
 * Created by doug on 18-2-27.
 * init in application
 */

public class InitContext {
    public static void setContext(Context context) {

        CommonContext.setContext(context);
    }

    public static Context getContext() {
        return CommonContext.getContext();
    }
}
