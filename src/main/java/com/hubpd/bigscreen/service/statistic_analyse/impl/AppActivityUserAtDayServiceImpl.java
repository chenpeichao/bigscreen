package com.hubpd.bigscreen.service.statistic_analyse.impl;

import com.hubpd.bigscreen.mapper.uar_statistic.AppActivityUserAtDayMapper;
import com.hubpd.bigscreen.service.statistic_analyse.AppActivityUserAtDayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 客户端累计用户数Service实现类
 *
 * @author ceek
 * @create 2019-08-19 11:28
 **/
@Service
public class AppActivityUserAtDayServiceImpl implements AppActivityUserAtDayService {
    private Logger logger = Logger.getLogger(AppActivityUserAtDayServiceImpl.class);

    @Autowired
    private AppActivityUserAtDayMapper appActivityUserAtDayMapper;

    /**
     * 查询指定机构的应用集合下，指定时间的累计用户数---当不传递标识截止昨日
     *
     * @param appKeySet 应用appkey集合
     * @param searchDay 查询日期(yyyyMMdd)
     * @return
     */
    public Long getTotalUserByAppKeySetAndSearchDay(Set<String> appKeySet, Long searchDay) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appKeySet", appKeySet);
        if (null != searchDay && searchDay > 0) {
            paramMap.put("searchDay", searchDay);
        }
        return appActivityUserAtDayMapper.getTotalUserByAppKeySetAndSearchDay(paramMap);
    }
}
