package com.hubpd.bigscreen.service.statistic_analyse;

import com.hubpd.bigscreen.dto.StatisticStatDTO;

import java.util.Set;

/**
 * 网站天统计指标service
 *
 * @author ceek
 * @create 2019-08-19 9:45
 **/
public interface WebAtCLNDayService {
    /**
     * 获取指定app集合在指定时间段的pv和uv统计数据
     *
     * @param appKeySet      应用appkey集合
     * @param searchBeginDay 查询起始时间(yyyyMMdd)
     * @param searchEndDay   查询截止时间(yyyyMMdd)
     * @return
     */
    public Long getTotalUserByOriginId(Set<String> appKeySet, Long searchBeginDay, Long searchEndDay);
}
