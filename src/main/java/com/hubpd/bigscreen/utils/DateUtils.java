package com.hubpd.bigscreen.utils;

import com.sun.xml.internal.ws.util.UtilException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具类
 * Created by ceek on 2018-08-09 22:37.
 */
public class DateUtils {

    /**
     * 获取前/后多少天的时间字符串
     *
     * @param sourceDate  初始时间
     * @param endGap      前后多少
     * @param datePattern 时间字符串返回模式(yyyy-MM-dd)
     * @return
     */
    public static String getBeforeDateStrByDateAndPattern(Date sourceDate, Integer endGap, String datePattern) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        Calendar c = Calendar.getInstance();
        c.setTime((Date) sourceDate.clone());
        c.add(Calendar.DATE, endGap);
        String endtimeStr = dateFormat.format(c.getTime());
        return endtimeStr;
    }

    /**
     * 获取指定日期指定格式的字符串日期
     *
     * @param date    日期
     * @param pattern 指定格式
     * @return
     */
    public static String getDateStrByDate(Date date, String pattern) {
        String formatDateStr = new SimpleDateFormat(pattern).format(date);
        return formatDateStr;
    }

    /**
     * 得到两个日期相隔天数
     *
     * @param firstDate  第一个时间
     * @param secondDate 第二个时间
     * @return
     */
    public static long getNumByTwoDate(Date firstDate, Date secondDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long firstTime = df.parse(df.format(firstDate)).getTime();
            long secondTime = df.parse(df.format(secondDate)).getTime();
            if (firstTime >= secondTime) {
                return (firstTime - secondTime) / (1000 * 60 * 60 * 24);
            } else {
                return (secondTime - firstTime) / (1000 * 60 * 60 * 24);
            }
        } catch (Exception e) {
            throw new UtilException(e);
        }
    }
}
