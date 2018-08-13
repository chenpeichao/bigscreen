package com.hubpd.bigscreen.controller;

import com.hubpd.bigscreen.service.uar_profile.UserAnalyseService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
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
     * 微信运营接口：微信公号、日期、新关注人数、取消关注人数、净增关注人数、累积关注人数、点赞总数； 数据是前一天往前7天的数据，每天调取1次，每个客户按最多10个公众号预估
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/getUserAnalyse")
    public Map<String, Object> getUserAnalyse(HttpServletRequest request, HttpServletResponse response){
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> resultMap = new HashMap<String, Object>();

        userAnalyseService.test();

        return resultMap;
    }
}
