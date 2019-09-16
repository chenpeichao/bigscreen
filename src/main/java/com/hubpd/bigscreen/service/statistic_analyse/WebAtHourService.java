package com.hubpd.bigscreen.service.statistic_analyse;

import com.hubpd.bigscreen.dto.WebAtHourDTO;

import java.util.List;
import java.util.Set;

/**
 * 网站的pv、uv统计数据service
 *
 * @author ceek
 * @create 2019-09-16 10:30
 **/
public interface WebAtHourService {
    /**
     * 获取当天统计的最大小时时间
     *
     * @return
     */
    public Integer getCurrentDayMaxHour(Set<String> appKeySet);

    /**
     * 获取指定应用集合在指定时间的统计数据
     *
     * @param appKeySet 应用appkey集合
     * @param day       天
     * @param hour      小时
     * @return
     */
    public WebAtHourDTO getStatByAppkeyAndDayAndBeforeHour(Set<String> appKeySet, String day, String hour);
}
