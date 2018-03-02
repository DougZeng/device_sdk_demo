package com.wesine.device_sdk.utils;

import java.util.regex.Pattern;

/**
 * Created by doug on 18-2-27.
 */

public class Utils {
    public static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";

    public static boolean isURL(final CharSequence input) {
        return isMatch(REGEX_URL, input);
    }

    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }
}
