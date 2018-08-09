package com.hubpd.bigscreen.utils;

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
     * @param sourceDate        初始时间
     * @param endGap            前后多少
     * @param datePattern       时间字符串返回模式(yyyy-MM-dd)
     * @return
     */
    public static String getBeforeDateStrByDateAndPattern(Date sourceDate, Integer endGap, String datePattern) throws Exception{
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        Calendar c = Calendar.getInstance();
        c.setTime((Date) sourceDate.clone());
        c.add(Calendar.DATE, endGap);
        String endtimeStr = dateFormat.format(c.getTime());
        return endtimeStr;
    }
}
