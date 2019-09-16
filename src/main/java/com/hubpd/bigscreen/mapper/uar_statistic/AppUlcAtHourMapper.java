package com.hubpd.bigscreen.mapper.uar_statistic;

import com.hubpd.bigscreen.bean.uar_statistic.AppUlcAtHour;
import com.hubpd.bigscreen.bean.uar_statistic.AppUlcAtHourKey;

import java.util.Map;

public interface AppUlcAtHourMapper {
    int deleteByPrimaryKey(AppUlcAtHourKey key);

    int insert(AppUlcAtHour record);

    int insertSelective(AppUlcAtHour record);

    AppUlcAtHour selectByPrimaryKey(AppUlcAtHourKey key);

    int updateByPrimaryKeySelective(AppUlcAtHour record);

    int updateByPrimaryKey(AppUlcAtHour record);

    /**
     * 获取指定appkey集合的今日新增用户数
     *
     * @param paramMap
     * @return
     */
    public Long getTodayNewUser(Map<String, Object> paramMap);
}