package com.hubpd.bigscreen.controller;

import com.hubpd.bigscreen.bean.Test;
import com.hubpd.bigscreen.service.TestService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试类
 * Created by ceek on 2018-08-07 23:14.
 */
@Controller
@RequestMapping(value="/test")
public class TestController {
    @Autowired
    private TestService testService;

    @ResponseBody
    @PostMapping("/findOneById")
    public Map<String, Object> addUser(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String idStr = request.getParameter("id");
        Test oneById = testService.findOneById(Integer.parseInt(idStr));

        resultMap.put("data", oneById);
        return resultMap;
    }
}
