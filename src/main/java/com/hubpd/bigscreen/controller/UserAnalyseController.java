package com.hubpd.bigscreen.controller;

import com.hubpd.bigscreen.service.uar_profile.UserAnalyseService;
import com.hubpd.bigscreen.utils.Constants;
import com.hubpd.bigscreen.utils.ErrorCode;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
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

/**
 * 用户分析--性别，年龄，地域(-泉州晚报大屏接口)
 *
 * @author cpc
 * @create 2018-08-11 19:42
 **/
@Controller
@RequestMapping(value="/ua")
public class UserAnalyseController {
    private Logger logger = Logger.getLogger(WXController.class);

    @Autowired
    private UserAnalyseService userAnalyseService;

    /**
     * 用户分析接口，计算性别，青老中，前5地域
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/getUserAnalyse")
    public Map<String, Object> getUserAnalyse(HttpServletRequest request, HttpServletResponse response){
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> resultMap = new HashMap<String, Object>();

        String orginIdStr = request.getParameter("orginId");
        if (StringUtils.isBlank(orginIdStr)) {
            resultMap.put("code", 0);
            resultMap.put("message", "机构id未传递");
            return resultMap;
        }

        orginIdStr = orginIdStr.trim();
        logger.info("用户分析接口调用，机构id为【" + orginIdStr + "】");

        try {
            return userAnalyseService.getUserAnalyseReturnData(orginIdStr, Constants.USER_PROFILE_REGIN_DATA_LEVEL_ES);
        } catch (Exception e) {
            logger.error("getUserAnalyse用户分析接口调用失败-发生未知错误", e);
            resultMap.put("code", 0);
            resultMap.put("message", "接口调用失败，请重试！！");
            return resultMap;
        }
    }

    /**
     * 用户分析接口，计算性别，青老中，前5地域
     *
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/getUserAnalyseNew")
    public Map<String, Object> getUserAnalyseNew(HttpServletRequest request, HttpServletResponse response) {
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> resultMap = new HashMap<String, Object>();

        String orginIdStr = request.getParameter("orginId");
        if (StringUtils.isBlank(orginIdStr)) {
            resultMap.put("code", 0);
            resultMap.put("message", "机构id未传递");
            return resultMap;
        }

        orginIdStr = orginIdStr.trim();
        logger.info("用户分析接口调用，机构id为【" + orginIdStr + "】");

        try {
            return userAnalyseService.getUserAnalyseReturnData(orginIdStr, Constants.USER_PROFILE_REGIN_DATA_LEVEL_MYSQL);
        } catch (Exception e) {
            logger.error("getUserAnalyse用户分析接口调用失败-发生未知错误", e);
            resultMap.put("code", 0);
            resultMap.put("message", "接口调用失败，请重试！！");
            return resultMap;
        }
    }

    /**
     * 用户画像---全省份信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/userPortraitAgeAndGenderAndProvinceRegion")
    public Map<String, Object> userPortraitAgeAndGenderAndProvinceRegion(HttpServletRequest request, HttpServletResponse response) {
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> resultMap = new HashMap<String, Object>();

        logger.info("request param 【" + request.getQueryString() + "】");

        String orginIdStr = StringUtils.isNotBlank(request.getParameter("orginId")) ? request.getParameter("orginId").trim() : request.getParameter("orginId");

        if (StringUtils.isBlank(orginIdStr)) {
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
            resultMap.put("message", "request param orginIdStr lack");
            return resultMap;
        }

        try {
            return userAnalyseService.getUserAnalyseAllRegion(orginIdStr);
        } catch (Exception e) {
            logger.error("用户画像全省份地域排行接口调用失败-发生未知错误", e);
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
            resultMap.put("message", "接口调用失败，请重试");
            return resultMap;
        }
    }
}
