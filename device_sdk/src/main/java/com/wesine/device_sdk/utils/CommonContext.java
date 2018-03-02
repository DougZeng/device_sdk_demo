package com.wesine.device_sdk.utils;

import android.content.Context;

/**
 * Created by doug on 18-2-27.
 */

public class CommonContext {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        CommonContext.context = context;
    }
}
