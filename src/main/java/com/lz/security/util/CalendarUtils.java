package com.lz.security.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 日历时间工具
 */
public class CalendarUtils {

    /**
     *
     * @param amount
     * @return
     */
    public static Date plusMinutes(int amount) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, amount);
        return nowTime.getTime();
    }

    public static Date plusSeconds(int amount) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.SECOND, amount);
        return nowTime.getTime();
    }

    public static Date plusMillisecond(int amount) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MILLISECOND, amount);
        return nowTime.getTime();
    }
}
