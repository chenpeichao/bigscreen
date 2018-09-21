package com.hubpd.bigscreen.controller;

import com.hubpd.bigscreen.service.statistic_analyse.StatisticAnalyseService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 运营分析
 *
 * @author cpc
 * @create 2018-09-10 15:39
 **/
@Controller
@RequestMapping(value = "/statistic")
public class StatisticAnalyseController {
    private Logger logger = Logger.getLogger(StatisticAnalyseController.class);

    @Autowired
    private StatisticAnalyseService statisticAnalyseService;

    /**
     * 运营分析--根据机构id和查询时间查询pv、uv以及crt的相关原创数和转载数---默认查询昨天
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/getStatisticAnalyse")
    public Map<String, Object> getStatisticAnalyse(HttpServletRequest request, HttpServletResponse response) {
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> resultMap = new HashMap<String, Object>();

        Date currentDate = new Date();

        String orginIdStr = request.getParameter("orginId");
        String searchDateStr = request.getParameter("searchDate");
        if (StringUtils.isBlank(orginIdStr)) {
            resultMap.put("code", 0);
            resultMap.put("message", "机构id未传递");
            return resultMap;
        }

        Date searchDate = currentDate;
        if (StringUtils.isNotBlank(searchDateStr)) {
            try {
                //获取当前天的整点时间的毫秒数
                long zero = System.currentTimeMillis() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
                searchDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDateStr.trim());
                //当查询时间大于当天系统时间，默认查询昨天天
                if (searchDate.getTime() >= zero) {
                    searchDate = DateUtils.addDays(currentDate, -1);
                }
                //只能获取最近30天的数据
                if (com.hubpd.bigscreen.utils.DateUtils.getNumByTwoDate(searchDate, currentDate) > 30) {
                    resultMap.put("code", 0);
                    resultMap.put("message", "查询时间必须为30天内数据！");
                    return resultMap;
                }
            } catch (ParseException e) {
                logger.error("getStatisticAnalyse运营分析接口日期参数格式错误", e);
                resultMap.put("code", 0);
                resultMap.put("message", "日期格式错误！【yyyy-MM-dd】");
                return resultMap;
            }
        } else {
            searchDate = DateUtils.addDays(currentDate, -1);
        }


        orginIdStr = orginIdStr.trim();
        logger.info("运营分析pv/uv/原创/转载分析接口调用，机构id为【" + orginIdStr + "】");

        try {
            return statisticAnalyseService.getStatisticAnalyse(orginIdStr, searchDate);
        } catch (Exception e) {
            logger.error("getStatisticAnalyse运营分析接口调用失败-发生未知错误", e);
            resultMap.put("code", 0);
            resultMap.put("message", "接口调用失败，请重试！！");
            return resultMap;
        }
    }
}
