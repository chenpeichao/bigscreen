package com.hubpd.bigscreen.service.statistic_analyse;

import java.util.Date;
import java.util.Map;

/**
 * 运营分析相关
 *
 * @author cpc
 * @create 2018-09-13 15:49
 **/
public interface StatisticAnalyseService {
    /**
     * 根据机构id和查询时间查询pv、uv以及crt的相关原创数和转载数---默认查询昨天
     *
     * @param orginIdStr
     * @param searchDate
     * @return
     */
    public Map<String, Object> getStatisticAnalyse(String orginIdStr, Date searchDate);
}