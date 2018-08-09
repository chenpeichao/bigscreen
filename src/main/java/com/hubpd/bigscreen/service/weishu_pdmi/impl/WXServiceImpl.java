package com.hubpd.bigscreen.service.weishu_pdmi.impl;

import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import com.hubpd.bigscreen.service.weishu_pdmi.WXService;
import com.hubpd.bigscreen.service.weishu_pdmi.WeiShuPdmiUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 微信service
 *
 * @author cpc
 * @create 2018-08-09 11:08
 **/
@Service
@Transactional
public class WXServiceImpl implements WXService {
    private Logger logger = Logger.getLogger(WXServiceImpl.class);

    /** uar_basic用户service */
    @Autowired
    private UarBasicUserService uarBasicUserService;
    /** wei_shu对应的微信用户service */
    @Autowired
    private WeiShuPdmiUserService weiShuPdmiUserService;

    /**
     * 根据机构id查询其下用户授权的公众号的前7天的公众号用户分析信息
     * @param orginId       机构Id
     * @param searchDate    查询日期  yyyy-MM-dd
     * @return
     */
    public Map<String, Object> getWXUserAnalyse(String orginId, Date searchDate) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        //查询uar环境中指定机构下的用户id列表
        List<String> uarBasicUserIdListByOrginId = uarBasicUserService.findUarBasicUserIdListByOrginId(orginId);

        //根据用户id列表查询其对应的公众号列表（1:自有，2：关注）
        List<Integer> pubAccountIdListByUserIdList = weiShuPdmiUserService.findPubAccountIdListByUserIdList(uarBasicUserIdListByOrginId, 1);


        return null;
    }
}
