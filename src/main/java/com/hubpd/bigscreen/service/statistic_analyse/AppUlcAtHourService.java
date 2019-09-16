package com.hubpd.bigscreen.service.statistic_analyse;

import java.util.Map;
import java.util.Set;

/**
 * 客户端今日新增用户Service
 *
 * @author ceek
 * @create 2019-09-16 14:33
 **/
public interface AppUlcAtHourService {
    /**
     * 获取指定appkey集合的今日新增用户数
     *
     * @param appKeySet 客户端appkey集合
     * @return
     */
    public Long getTodayNewUser(Set<String> appKeySet);
}
