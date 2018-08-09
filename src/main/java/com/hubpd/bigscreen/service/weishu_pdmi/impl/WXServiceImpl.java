package com.hubpd.bigscreen.service.weishu_pdmi.impl;

import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import com.hubpd.bigscreen.service.weishu_pdmi.WXService;
import com.hubpd.bigscreen.service.weishu_pdmi.WeiShuPdmiUserService;
import com.hubpd.bigscreen.utils.Constants;
import com.hubpd.bigscreen.utils.DateUtils;
import com.hubpd.bigscreen.vo.WXUserAnalyseVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hubpd.bigscreen.utils.DateUtils.getBeforeDateStrByDateAndPattern;

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
        String beginDateStr = "";
        String endDateStr = "";
        try {
            beginDateStr = DateUtils.getBeforeDateStrByDateAndPattern(searchDate, Constants.DATA_BACK_BEGIN_DAY_NUM, "yyyy-MM-dd");
            endDateStr = DateUtils.getBeforeDateStrByDateAndPattern(searchDate, Constants.DATA_BACK_END_DAY_NUM, "yyyy-MM-dd");
        } catch (Exception e) {
            logger.error("请求参数，时间转换错误", e);
            resultMap.put("code", 0);
            resultMap.put("message", "接口调用失败，时间参数传递错误！！");
            return resultMap;
        }

        try {
            List<WXUserAnalyseVO> wxUserAnalyseVOList = weiShuPdmiUserService.findUserAnalyseByPubAccountIdListAndSearchDate(pubAccountIdListByUserIdList, beginDateStr, endDateStr);
            resultMap.put("code", 1);
            resultMap.put("data", wxUserAnalyseVOList);
        } catch (Exception e) {
            logger.error("查询出错，请稍后再试！", e);
            resultMap.put("code", 0);
            resultMap.put("message", "查询出错，请稍后再试！");
        } finally {
            return resultMap;
        }
    }
}
