package com.hubpd.bigscreen.utils;

import com.sun.xml.internal.ws.util.UtilException;

import java.text.DateFormat;
import java.text.ParseException;
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

    /**
     * 得到指定日期字符串的前一天指定格式的时间字符串
     *
     * @param sourceDateStr 原始日期
     * @param sourcePattern 原始日期格式
     * @param targetPattern 目标日期格式字符串
     * @return
     */
    public static String getYesterdayDateStr(String sourceDateStr, String sourcePattern, String targetPattern) {
        SimpleDateFormat sourceSdf = new SimpleDateFormat(sourcePattern);
        SimpleDateFormat targetSdf = new SimpleDateFormat(targetPattern);
        try {
            return targetSdf.format(DateUtils.addDay(sourceSdf.parse(sourceDateStr), -1));
        } catch (ParseException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 日加减 by spt
     *
     * @return
     */
    public static Date addDay(Date date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, n);
        return c.getTime();
    }

    /**
     * 获取查询时间指定相差天数的日期字符串
     *
     * @param searchDateStr 查询日期字符串
     * @param rangeDay      相差天数
     * @param sourcePattern 查询日期字符串格式
     * @param targetPattern 结果日期字符串格式
     * @return
     */
    public static String getRangeDay(String searchDateStr, Integer rangeDay, String sourcePattern, String targetPattern) {
        Calendar no = Calendar.getInstance();
        try {
            no.setTime(new SimpleDateFormat(sourcePattern).parse(searchDateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        no.set(Calendar.DATE, no.get(Calendar.DATE) + rangeDay);
        return new SimpleDateFormat(targetPattern).format(no.getTime());
    }

    /**
     * 获取查询时间指定相差天数的日期字符串
     *
     * @param date          查询日期
     * @param rangeDay      相差天数
     * @param targetPattern 结果日期字符串格式
     * @return
     */
    public static String getRangeDay(Date date, Integer rangeDay, String targetPattern) {
        Calendar no = Calendar.getInstance();
        no.setTime(date);
        no.set(Calendar.DATE, no.get(Calendar.DATE) + rangeDay);
        return new SimpleDateFormat(targetPattern).format(no.getTime());
    }
}
