package com.hubpd.bigscreen.service.statistic_analyse.impl;

import com.hubpd.bigscreen.mapper.uar_statistic.AppUlcAtHourMapper;
import com.hubpd.bigscreen.service.statistic_analyse.AppUlcAtHourService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 客户端今日新增用户Service实现类
 *
 * @author ceek
 * @create 2019-09-16 14:34
 **/
@Service
public class AppUlcAtHourServiceImpl implements AppUlcAtHourService {
    private Logger logger = Logger.getLogger(AppUlcAtHourServiceImpl.class);

    @Autowired
    private AppUlcAtHourMapper appUlcAtHourMapper;

    /**
     * 获取指定appkey集合的今日新增用户数
     *
     * @param appKeySet 客户端appkey集合
     * @return
     */
    public Long getTodayNewUser(Set<String> appKeySet) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appKeySet", appKeySet);
        return appUlcAtHourMapper.getTodayNewUser(paramMap);
    }
}
