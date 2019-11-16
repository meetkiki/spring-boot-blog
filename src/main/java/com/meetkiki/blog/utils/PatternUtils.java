//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.meetkiki.blog.utils;

import java.util.regex.Pattern;

public final class PatternUtils {
    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String IP_REGEX = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private static final String URL_REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private static final String MOBILE_REGEX = "^(13[0-9]|14[57]|15[012356789]|17[0678]|18[0-9])[0-9]{8}$";

    public static boolean isEmail(String email) {
        return isMatch("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", email);
    }

    public static boolean isIdCard18(String idCard) {
        String regex = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
        return isMatch(regex, idCard);
    }

    public static boolean isIdCard15(String idCard) {
        String regex = "^[1-9]\\\\d{7}((0\\\\d)|(1[0-2]))(([0|1|2]\\\\d)|3[0-1])\\\\d{3}$";
        return isMatch(regex, idCard);
    }

    public static boolean isImage(String suffix) {
        if (null != suffix && !"".equals(suffix) && suffix.contains(".")) {
            String regex = "(.*?)(?i)(jpg|jpeg|png|gif|bmp|webp)";
            return isMatch(regex, suffix);
        } else {
            return false;
        }
    }

    public static boolean isMobile(String mobile) {
        return isMatch("^(13[0-9]|14[57]|15[012356789]|17[0678]|18[0-9])[0-9]{8}$", mobile);
    }

    public static boolean isPhone(String phone) {
        return isMatch("(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$", phone);
    }

    public static boolean isDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return isMatch(regex, digit);
    }

    public static boolean isDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return isMatch(regex, decimals);
    }

    public static boolean isBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return isMatch(regex, blankSpace);
    }

    public static boolean isChinese(String chinese) {
        String regex = "^[一-龥]+$";
        return isMatch(regex, chinese);
    }

    public static boolean isRealName(String chinese) {
        String regex = "^[A-Za-z0-9\\s一-龥]+$";
        return isMatch(regex, chinese);
    }

    public static boolean isNumber(String str) {
        String regex = "^[1-9]\\d*$";
        return isMatch(regex, str);
    }

    public static boolean isBirthday(String birthday) {
        String regex = "^(\\d{4})-(\\d{2})-(\\d{2})$";
        return isMatch(regex, birthday);
    }

    public static boolean isURL(String url) {
        return isMatch("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", url);
    }

    public static boolean isPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return isMatch(regex, postcode);
    }

    public static boolean isIpAddress(String ipAddress) {
        return isMatch("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", ipAddress);
    }

    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    private PatternUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
