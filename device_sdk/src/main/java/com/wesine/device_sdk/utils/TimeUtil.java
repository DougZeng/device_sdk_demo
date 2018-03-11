package com.wesine.device_sdk.utils;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by doug on 18-2-26.
 */

public class TimeUtil {

    private static final SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static String getUnixTimeStamp() {
        String unixtimestamp = "";
        try {
            long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
            unixtimestamp = String.valueOf(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unixtimestamp;
    }

    /**
     * get current date and time as String
     *
     * @return
     */
    public static String getDateTimeString() {
        GregorianCalendar now = new GregorianCalendar();
        return mDateTimeFormat.format(now.getTime());
    }

    /**
     * get current date as String
     *
     * @return
     */
    public static String getDateString() {
        GregorianCalendar now = new GregorianCalendar();
        return mDateFormat.format(now.getTime());
    }

    public static void main(String[] args) {
        System.out.println(getDateString());
    }
}
