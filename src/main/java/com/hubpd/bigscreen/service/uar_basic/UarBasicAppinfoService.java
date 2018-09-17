package com.hubpd.bigscreen.service.uar_basic;

import com.hubpd.bigscreen.bean.uar_basic.UarBasicAppinfo;

import java.util.List;
import java.util.Map;

/**
 * 应用appinfo的相关操作
 *
 * @author cpc
 * @create 2018-09-05 16:34
 **/
public interface UarBasicAppinfoService {
    /**
     * 根据机构id，查询其下对应的所有网站和移动应用的appaccount(即应用标识at)（返回结果为map<应用中文名，(应用appaccount，当为移动应用时为，Android和ios的appkey)>）
     *
     * @param orgId 机构id
     * @return
     */
    public Map<String, List<String>> findAppaccountListByOrgId(String orgId);

    /**
     * 根据应用at，查询应用详情
     *
     * @param appaccount 应用at
     * @return
     */
    public UarBasicAppinfo findAppInfoByAppAccountOrAppAccount2(String appaccount);
}
