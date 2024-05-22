package com.socialnetwork.socialnetworkjavaspring.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {
    public static TimeZone TIMEZONE_VN = TimeZone.getTimeZone("GMT+07:00");
    public static final String FORMAT_DATE_TIME5 = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat formatter = new SimpleDateFormat();

    public static String dateToString(Date date, String format, TimeZone timeZone) {
        if (date == null)
            return null;
        formatter.setTimeZone(timeZone);
        formatter.applyPattern(format);
        return formatter.format(date);
    }
}
