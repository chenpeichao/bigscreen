package com.hubpd.bigscreen.service.weishu_pdmi;

import java.util.Date;
import java.util.Map;

/**
 * 微信service
 *
 * @author cpc
 * @create 2018-08-09 11:08
 **/
public interface WXService {
    /**
     * 根据机构id查询其下用户授权的公众号的前7天的公众号用户分析信息
     * @param orginId       机构Id
     * @param searchDate    查询日期  yyyy-MM-dd
     * @return
     */
    public Map<String, Object> getWXUserAnalyse(String orginId, Date searchDate);
}
