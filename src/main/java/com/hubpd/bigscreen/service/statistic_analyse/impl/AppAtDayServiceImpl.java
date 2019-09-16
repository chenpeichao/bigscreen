package com.hubpd.bigscreen.service.statistic_analyse.impl;

import com.hubpd.bigscreen.dto.AppAtDayDTO;
import com.hubpd.bigscreen.mapper.uar_statistic.AppAtDayMapper;
import com.hubpd.bigscreen.service.statistic_analyse.AppAtDayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 客户端昨日及以前的新增用户及活跃用户Service实现类
 *
 * @author ceek
 * @create 2019-09-16 14:50
 **/
@Service
public class AppAtDayServiceImpl implements AppAtDayService {
    private Logger logger = Logger.getLogger(AppAtDayServiceImpl.class);

    @Autowired
    private AppAtDayMapper appAtDayMapper;

    /**
     * 获取指定appkey的应用在指定时间的新增和活跃用户
     *
     * @param appKeySet 客户端appkey集合
     * @param searchDay 查询日期
     * @return
     */
    public AppAtDayDTO getNewUVAndActiveUVByAppKeySetAndDay(Set<String> appKeySet, String searchDay) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appKeySet", appKeySet);
        paramMap.put("searchDay", searchDay);
        return appAtDayMapper.getNewUVAndActiveUVByAppKeySetAndDay(paramMap);
    }
}
