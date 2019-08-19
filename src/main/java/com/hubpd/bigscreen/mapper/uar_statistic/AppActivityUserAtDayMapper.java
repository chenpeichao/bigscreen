package com.hubpd.bigscreen.mapper.uar_statistic;

import com.hubpd.bigscreen.bean.uar_statistic.AppActivityUserAtDay;
import com.hubpd.bigscreen.bean.uar_statistic.AppActivityUserAtDayKey;

import java.util.Map;

public interface AppActivityUserAtDayMapper {
    int deleteByPrimaryKey(AppActivityUserAtDayKey key);

    int insert(AppActivityUserAtDay record);

    int insertSelective(AppActivityUserAtDay record);

    AppActivityUserAtDay selectByPrimaryKey(AppActivityUserAtDayKey key);

    int updateByPrimaryKeySelective(AppActivityUserAtDay record);

    int updateByPrimaryKey(AppActivityUserAtDay record);

    /**
     * 查询指定机构的应用集合下，指定时间的累计用户数---当不传递标识截止昨日
     *
     * @param appKeySet 应用appkey集合
     * @param searchDay 查询日期(yyyyMMdd)
     * @return
     */
    public Long getTotalUserByAppKeySetAndSearchDay(Map<String, Object> paramMap);
}