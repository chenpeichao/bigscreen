package com.hubpd.bigscreen.service.statistic_analyse;

import com.hubpd.bigscreen.dto.AppAtDayDTO;

import java.util.Set;

/**
 * 客户端昨日及以前的新增用户及活跃用户Service
 *
 * @author ceek
 * @create 2019-09-16 14:50
 **/
public interface AppAtDayService {
    /**
     * 获取指定appkey的应用在指定时间的新增和活跃用户
     *
     * @param appKeySet 客户端appkey集合
     * @param searchDay 查询日期
     * @return
     */
    public AppAtDayDTO getNewUVAndActiveUVByAppKeySetAndDay(Set<String> appKeySet, String searchDay);
}
