package com.hubpd.bigscreen.controller;

import com.hubpd.bigscreen.service.weishu_pdmi.WXService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信分析-泉州晚报大屏接口
 * Created by ceek on 2018-08-07 23:14.
 */
@Controller
@RequestMapping(value="/wx")
public class WXController {
    private Logger logger = Logger.getLogger(WXController.class);

    @Autowired
    private WXService wxService;

    /**
     * 微信运营接口：微信公号、日期、新关注人数、取消关注人数、净增关注人数、累积关注人数、点赞总数； 数据是前一天往前7天的数据，每天调取1次，每个客户按最多10个公众号预估
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/getWXUserAnalyse")
    public Map<String, Object> getWXUserAnalyse(HttpServletRequest request, HttpServletResponse response){
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> resultMap = new HashMap<String, Object>();

        String orginIdStr = request.getParameter("orginId");
        String searchDateStr = request.getParameter("searchDate");

        Date searchDate = null;
        if(StringUtils.isBlank(orginIdStr)) {
            resultMap.put("code", 0);
            resultMap.put("message", "机构id未传递");
            return resultMap;
        }
        if(StringUtils.isBlank(searchDateStr)) {
            searchDate = new Date();
        } else {
            try {
                searchDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDateStr.trim());
                //当查询时间大于当天系统时间，默认查询今天
                if(searchDate.getTime() > System.currentTimeMillis()) {
                    searchDate =  new Date();
                }
            } catch (ParseException e) {
                logger.error("getWXUserAnalyse微信运营接口日期参数格式错误", e);
                resultMap.put("code", 0);
                resultMap.put("message", "日期格式错误！【yyyy-MM-dd】");
                return resultMap;
            }
        }

        orginIdStr = orginIdStr.trim();
        logger.info("微信运营接口调用，机构id为【" + orginIdStr + "】");

        try {
            return wxService.getWXUserAnalyse(orginIdStr, searchDate);
        } catch (Exception e) {
            logger.error("getWXUserAnalyse微信运营接口调用失败-发生未知错误", e);
            resultMap.put("code", 0);
            resultMap.put("message", "接口调用失败，请重试！！");
            return resultMap;
        }
    }


    /**
     * 内容ID、文章标题、文章链接（固定连接）、阅读数、点赞数
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/getWXArticleList")
    public Map<String, Object> getWXArticleList(HttpServletRequest request, HttpServletResponse response){
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> resultMap = new HashMap<String, Object>();

        String orginIdStr = request.getParameter("orginId");
        String searchDateStr = request.getParameter("searchDate");

        Date searchDate = null;
        if(StringUtils.isBlank(orginIdStr)) {
            resultMap.put("code", 0);
            resultMap.put("message", "机构id未传递");
            return resultMap;
        }
        if(StringUtils.isBlank(searchDateStr)) {
            searchDate = new Date();
        } else {
            try {
                searchDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDateStr.trim());
                //当查询时间大于当天系统时间，默认查询今天
                if(searchDate.getTime() > System.currentTimeMillis()) {
                    searchDate =  new Date();
                }
            } catch (ParseException e) {
                logger.error("getWXUserAnalyse微信内容接口日期参数格式错误", e);
                resultMap.put("code", 0);
                resultMap.put("message", "日期格式错误！【yyyy-MM-dd】");
                return resultMap;
            }
        }

        orginIdStr = orginIdStr.trim();
        logger.info("微信内容接口调用，机构id为【" + orginIdStr + "】");

        try {
            return wxService.getWXArticleList(orginIdStr, searchDate);
        } catch (Exception e) {
            logger.error("getWXUserAnalyse微信内容接口调用失败-发生未知错误", e);
            resultMap.put("code", 0);
            resultMap.put("message", "接口调用失败，请重试！！");
            return resultMap;
        }
    }
}
