package com.hubpd.bigscreen.service.statistic_analyse.impl;

import com.hubpd.bigscreen.mapper.uar_statistic.WebAtClNDayMapper;
import com.hubpd.bigscreen.service.statistic_analyse.WebAtCLNDayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 网站天统计指标service实现类
 *
 * @author ceek
 * @create 2019-08-19 9:46
 **/
@Service
public class WebAtCLNDayServiceImpl implements WebAtCLNDayService {
    private Logger logger = Logger.getLogger(WebAtCLNDayServiceImpl.class);

    @Autowired
    private WebAtClNDayMapper webAtClNDayMapper;

    /**
     * 获取指定app集合在指定时间段的累计用户数
     *
     * @param appKeySet      应用appkey集合
     * @param searchBeginDay 查询起始时间(yyyyMMdd)
     * @param searchEndDay   查询截止时间(yyyyMMdd)
     * @return
     */
    public Long getTotalUserByOriginId(Set<String> appKeySet, Long searchBeginDay, Long searchEndDay) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appKeySet", appKeySet);
        if (null != searchBeginDay && searchBeginDay > 0) {
            paramMap.put("searchBeginDay", searchBeginDay);
        }
        if (null != searchEndDay && searchEndDay > 0) {
            paramMap.put("searchEndDay", searchEndDay);
        }
        return webAtClNDayMapper.getTotalUserByOriginId(paramMap);
    }
}
