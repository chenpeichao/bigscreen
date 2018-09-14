package com.hubpd.bigscreen.mapper.uar_statistic;

import com.hubpd.bigscreen.bean.uar_statistic.UarStatisticWebAtDay;
import com.hubpd.bigscreen.bean.uar_statistic.UarStatisticWebAtDayKey;

import java.util.List;
import java.util.Map;

public interface UarStatisticWebAtDayMapper {
    int deleteByPrimaryKey(UarStatisticWebAtDayKey key);

    int insert(UarStatisticWebAtDay record);

    int insertSelective(UarStatisticWebAtDay record);

    UarStatisticWebAtDay selectByPrimaryKey(UarStatisticWebAtDayKey key);

    int updateByPrimaryKeySelective(UarStatisticWebAtDay record);

    int updateByPrimaryKey(UarStatisticWebAtDay record);

    /**
     * 根据at和时间查询统计信息
     *
     * @param appkey        应用appkey
     * @param searchDateStr 查询时间字符串
     * @return
     */
    public UarStatisticWebAtDay selectPVAndUVByAtAndDate(Map<String, Object> params);
}