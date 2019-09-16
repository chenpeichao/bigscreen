package com.hubpd.bigscreen.mapper.uar_statistic;

import com.hubpd.bigscreen.bean.uar_statistic.WebAtHour;
import com.hubpd.bigscreen.bean.uar_statistic.WebAtHourKey;
import com.hubpd.bigscreen.dto.WebAtHourDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface WebAtHourMapper {
    int deleteByPrimaryKey(WebAtHourKey key);

    int insert(WebAtHour record);

    int insertSelective(WebAtHour record);

    WebAtHour selectByPrimaryKey(WebAtHourKey key);

    int updateByPrimaryKeySelective(WebAtHour record);

    int updateByPrimaryKey(WebAtHour record);

    /**
     * 获取当天统计的最大小时时间
     *
     * @return
     */
    public Integer getCurrentDayMaxHour(Map<String, Object> paramMap);

    /**
     * 获取指定应用集合在指定时间的统计数据
     *
     * @param appKeySet 应用appkey集合
     * @param day       天
     * @param hour      小时
     * @return
     */
    public WebAtHourDTO getStatByAppkeyAndDayAndBeforeHour(Map<String, Object> paramMap);
}