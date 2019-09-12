package com.hubpd.bigscreen.controller;

import com.hubpd.bigscreen.service.statistic_analyse.StatisticAnalyseService;
import com.hubpd.bigscreen.utils.Constants;
import com.hubpd.bigscreen.utils.ErrorCode;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

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

    /**
     * 热门文章
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/getHotArticleRank")
    public Map<String, Object> getHotArticleRank(HttpServletRequest request, HttpServletResponse response) {
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        logger.info("调用/statistic/getHotArticleRank接口request param 【" + request.getQueryString() + "】");
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String orginIdStr = StringUtils.isNotBlank(request.getParameter("orginId")) ? request.getParameter("orginId").trim() : request.getParameter("orginId");
        String appFlagStr = StringUtils.isNotBlank(request.getParameter("appFlag")) ? request.getParameter("appFlag").trim() : request.getParameter("appFlag");
        String startSearchDateStr = StringUtils.isNotBlank(request.getParameter("startSearchDate")) ? request.getParameter("startSearchDate").trim() : request.getParameter("startSearchDate");
        String endSearchDateStr = StringUtils.isNotBlank(request.getParameter("endSearchDate")) ? request.getParameter("endSearchDate").trim() : request.getParameter("endSearchDate");
        String topNStr = StringUtils.isNotBlank(request.getParameter("topN")) ? request.getParameter("topN").trim() : request.getParameter("topN");

        if (StringUtils.isBlank(orginIdStr)) {
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
            resultMap.put("message", "request param orginId lack");
            return resultMap;
        }

        //appFlag未传递时，以及格式不为1(网站),2(客户端)
        Pattern appFlagPattern = Pattern.compile("^[" + Constants.PARAM_APP_FLAG_ALL + "]$");
        Integer appFlag = Constants.UAR_APP_TYPE_WEB;
        if (StringUtils.isBlank(appFlagStr)) {
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
            resultMap.put("message", "request param appFlag lack");
            return resultMap;
        } else if (!appFlagPattern.matcher(appFlagStr).matches()) {
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_ERROR_PATTERN);
            resultMap.put("message", "request param appFlag value is wrong");
            return resultMap;
        } else {
            try {
                appFlag = Integer.parseInt(appFlagStr);
            } catch (NumberFormatException e) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_ERROR_PATTERN);
                resultMap.put("message", "request param appFlag value is wrong");
                return resultMap;
            }
        }

        if (StringUtils.isBlank(startSearchDateStr)) {
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_ERROR_PATTERN);
            resultMap.put("message", "request param startSearchDate lack");
            return resultMap;
        }
        if (StringUtils.isBlank(endSearchDateStr)) {
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_ERROR_PATTERN);
            resultMap.put("message", "request param endSearchDateStr lack");
            return resultMap;
        }

        //默认查询前8天到昨天的数据
        String endPublishTime = new SimpleDateFormat("yyyy-MM-dd").format((new DateTime()).minusDays(1).withHourOfDay(23).
                withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999).toDate());
        String startPublishTime = new SimpleDateFormat("yyyy-MM-dd").format((new DateTime()).minusDays(7).withHourOfDay(0).
                withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate());

        if (StringUtils.isNotBlank(startSearchDateStr)) {
            startPublishTime = startSearchDateStr.trim();
            try {
                new SimpleDateFormat("yyyy-MM-dd").parse(startPublishTime);
            } catch (ParseException e) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "起始时间格式错误【yyyy-MM-dd】");
                return resultMap;
            }
        }
        if (StringUtils.isNotBlank(endSearchDateStr)) {
            endPublishTime = endSearchDateStr.trim();
            try {
                new SimpleDateFormat("yyyy-MM-dd").parse(endPublishTime + "");
            } catch (ParseException e) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "截止时间格式错误【yyyy-MM-dd】");
                return resultMap;
            }
        }

        Integer topN = Constants.STATISTIC_APP_AND_WEB_HOT_ARTICLE_TOP_NUM;
        if (StringUtils.isNotBlank(topNStr)) {
            try {
                Integer paramTopN = Integer.parseInt(topNStr);
                if (paramTopN < 1 || paramTopN > Constants.STATISTIC_APP_AND_WEB_HOT_ARTICLE_TOP_NUM) {
                    resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                    resultMap.put("message", "topN格式错误,只能为1-" + Constants.STATISTIC_APP_AND_WEB_HOT_ARTICLE_TOP_NUM + "的整数");
                    return resultMap;
                } else {
                    topN = paramTopN;
                }
            } catch (NumberFormatException e) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "topN格式错误,只能为1-" + Constants.STATISTIC_APP_AND_WEB_HOT_ARTICLE_TOP_NUM + "的整数");
                return resultMap;
            }
        }

        try {
            return statisticAnalyseService.getHotArticleRank(orginIdStr, appFlag, startPublishTime.replace("-", ""), endPublishTime.replace("-", ""), topN);
        } catch (Exception e) {
            logger.error("getStatisticAnalyse运营分析接口调用失败-发生未知错误", e);
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
            resultMap.put("message", "接口调用失败，请重试！！");
            return resultMap;
        }
    }

    /**
     * 运营分析--根据机构id和查询时间查询pv、uv以及crt的相关原创数和转载数---默认查询昨天
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/getTotalUserByOriginId")
    public Map<String, Object> getTotalUserByOriginId(HttpServletRequest request, HttpServletResponse response) {
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        logger.info("调用/statistic/webGeneralView接口request param 【" + request.getQueryString() + "】");
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String orginIdStr = StringUtils.isNotBlank(request.getParameter("orginId")) ? request.getParameter("orginId").trim() : request.getParameter("orginId");
        String appFlagStr = StringUtils.isNotBlank(request.getParameter("appFlag")) ? request.getParameter("appFlag").trim() : request.getParameter("appFlag");
        String searchBeginDateStr = StringUtils.isNotBlank(request.getParameter("startSearchDate")) ? request.getParameter("startSearchDate").trim() : "2017-01-01";
        String searchEndDateStr = StringUtils.isNotBlank(request.getParameter("endSearchDate")) ? request.getParameter("endSearchDate").trim() : request.getParameter("endSearchDate");

        if (StringUtils.isBlank(orginIdStr)) {
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
            resultMap.put("message", "request param orginId lack");
            return resultMap;
        }

        //appFlag未传递时，以及格式不为1(网站),2(客户端)
        Pattern appFlagPattern = Pattern.compile("^[" + Constants.PARAM_APP_FLAG_ALL + "]$");
        Integer appFlag = Constants.UAR_APP_TYPE_WEB;
        if (StringUtils.isBlank(appFlagStr)) {
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
            resultMap.put("message", "request param appFlag lack");
            return resultMap;
        } else if (!appFlagPattern.matcher(appFlagStr).matches()) {
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_ERROR_PATTERN);
            resultMap.put("message", "request param appFlag value is wrong");
            return resultMap;
        } else {
            try {
                appFlag = Integer.parseInt(appFlagStr);
            } catch (NumberFormatException e) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_ERROR_PATTERN);
                resultMap.put("message", "request param appFlag value is wrong");
                return resultMap;
            }
        }

        String startPublishTime = "";
        String endPublishTime = "";

        Long searchBeginDay = 0l;
        Long searchEndDay = 0l;
        if (StringUtils.isNotBlank(searchBeginDateStr)) {
            startPublishTime = searchBeginDateStr.trim();
            try {
                new SimpleDateFormat("yyyy-MM-dd").parse(startPublishTime);
                searchBeginDay = Long.parseLong(startPublishTime.replace("-", ""));
            } catch (ParseException e) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "起始时间格式错误【yyyy-MM-dd】");
                return resultMap;
            }
        }
        if (StringUtils.isNotBlank(searchEndDateStr)) {
            endPublishTime = searchEndDateStr.trim();
            try {
                new SimpleDateFormat("yyyy-MM-dd").parse(endPublishTime);
                searchEndDay = Long.parseLong(endPublishTime.replace("-", ""));
            } catch (ParseException e) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "截止时间格式错误【yyyy-MM-dd】");
                return resultMap;
            }
        }

        try {
            return statisticAnalyseService.getTotalUserByOriginId(orginIdStr, appFlag, searchBeginDay, searchEndDay);
        } catch (Exception e) {
            logger.error("getStatisticAnalyse运营分析接口调用失败-发生未知错误", e);
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
            resultMap.put("message", "接口调用失败，请重试！！");
            return resultMap;
        }
    }

    /**
     * 运营分析--概览页统计数据
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/getStatisticAnalyseOverview")
    public Map<String, Object> getStatisticAnalyseOverview(HttpServletRequest request, HttpServletResponse response) {
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        logger.info("调用/statistic/webGeneralView接口request param 【" + request.getQueryString() + "】");
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String orginIdStr = StringUtils.isNotBlank(request.getParameter("orginId")) ? request.getParameter("orginId").trim() : request.getParameter("orginId");

        if (StringUtils.isBlank(orginIdStr)) {
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
            resultMap.put("message", "request param orginId lack");
            return resultMap;
        }

        try {
//            return statisticAnalyseService.getStatisticAnalyseOverview(orginIdStr);
            return null;
        } catch (Exception e) {
            logger.error("getStatisticAnalyse运营分析接口调用失败-发生未知错误", e);
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
            resultMap.put("message", "接口调用失败，请重试！！");
            return resultMap;
        }
    }
}
