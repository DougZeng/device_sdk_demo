package com.wesine.device_sdk.utils;

import java.io.File;

/**
 * Created by doug on 18-2-27.
 */

public class ImageUtils {
    public static File getNewImageFile() {
        File dir = Device.getApp().getExternalFilesDir("image");
        if (!dir.exists() && !dir.mkdirs()) {
            return null;
        }
        String name = MD5Utils.getMD5(String.format("Image.%d", System.currentTimeMillis()));
        File file = new File(dir, name + ".jpg");
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        return file;
    }
}
