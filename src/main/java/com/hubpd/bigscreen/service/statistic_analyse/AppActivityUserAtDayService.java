package com.hubpd.bigscreen.service.statistic_analyse;

import java.util.Set;

/**
 * 客户端累计用户数
 *
 * @author ceek
 * @create 2019-08-19 11:26
 **/
public interface AppActivityUserAtDayService {
    /**
     * 查询指定机构的应用集合下，指定时间的累计用户数---当不传递标识截止昨日
     *
     * @param appKeySet 应用appkey集合
     * @param searchDay 查询日期(yyyyMMdd)
     * @return
     */
    public Long getTotalUserByAppKeySetAndSearchDay(Set<String> appKeySet, Long searchDay);
}
