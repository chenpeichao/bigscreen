package com.hubpd.bigscreen.service.statistic_analyse.impl;

import com.hubpd.bigscreen.dto.WebAtHourDTO;
import com.hubpd.bigscreen.mapper.uar_statistic.WebAtHourMapper;
import com.hubpd.bigscreen.service.statistic_analyse.WebAtHourService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 网站的pv、uv统计数据service实现类
 *
 * @author ceek
 * @create 2019-09-16 10:30
 **/
@Service
public class WebAtHourServiceImpl implements WebAtHourService {
    private Logger logger = Logger.getLogger(WebAtHourServiceImpl.class);

    @Autowired
    private WebAtHourMapper webAtHourMapper;

    /**
     * 获取当天统计的最大小时时间
     *
     * @return
     */
    public Integer getCurrentDayMaxHour(Set<String> appKeySet) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appKeySet", appKeySet);
        return webAtHourMapper.getCurrentDayMaxHour(paramMap);
    }

    /**
     * 获取指定应用集合在指定时间的统计数据
     *
     * @param appKeySet 应用appkey集合
     * @param day       天
     * @param hour      小时
     * @return
     */
    public WebAtHourDTO getStatByAppkeyAndDayAndBeforeHour(Set<String> appKeySet, String day, String hour) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appKeySet", appKeySet);
        paramMap.put("day", day);
        paramMap.put("hour", hour);
        return webAtHourMapper.getStatByAppkeyAndDayAndBeforeHour(paramMap);
    }
}
