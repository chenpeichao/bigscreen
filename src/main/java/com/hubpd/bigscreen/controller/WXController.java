package com.hubpd.bigscreen.controller;

import com.github.pagehelper.PageInfo;
import com.hubpd.bigscreen.bean.uar_basic.UarBasicUser;
import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import com.hubpd.bigscreen.service.weishu_pdmi.WXService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试类
 * Created by ceek on 2018-08-07 23:14.
 */
@Controller
@RequestMapping(value="/wx")
public class WXController {
    private Logger logger = Logger.getLogger(WXController.class);

    @Autowired
    private WXService wxService;

    @ResponseBody
    @PostMapping("/getWXUserAnalyse")
    public Map<String, Object> getWXUserAnalyse(HttpServletRequest request){
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
                searchDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDateStr);
            } catch (ParseException e) {
                logger.error("getWXUserAnalyse微信运营接口日期参数格式错误", e);
                resultMap.put("code", 0);
                resultMap.put("message", "日期格式错误！【yyyy-MM-dd】");
                return resultMap;
            }
        }

        try {
            return wxService.getWXUserAnalyse(orginIdStr, searchDate);
        } catch (Exception e) {
            logger.error("getWXUserAnalyse微信运营接口调用失败-发生未知错误", e);
            resultMap.put("code", 0);
            resultMap.put("message", "接口调用失败，请重试！！");
            return resultMap;
        }
    }
}
