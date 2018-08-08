package com.hubpd.bigscreen.controller;

import com.hubpd.bigscreen.bean.uar_basic.UarBasicUser;
import com.hubpd.bigscreen.service.uar_basic.UarUserService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试类
 * Created by ceek on 2018-08-07 23:14.
 */
@Controller
@RequestMapping(value="/test")
public class TestController {
    @Autowired
    private UarUserService uarUserService;

    @ResponseBody
    @PostMapping("/findUarBasicUserListByOriginId")
    public Map<String, Object> findUarBasicUserListByOriginId(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String orgIdStr = request.getParameter("orgId");
        List<UarBasicUser> uarBasicUserListByOriginId = uarUserService.findUarBasicUserListByOriginId(orgIdStr);

        resultMap.put("data", uarBasicUserListByOriginId);
        return resultMap;
    }
}
