package com.hubpd.bigscreen.service.uar_basic.impl;

import com.hubpd.bigscreen.bean.uar_basic.UarBasicAppinfo;
import com.hubpd.bigscreen.mapper.uar_basic.UarBasicAppinfoMapper;
import com.hubpd.bigscreen.service.uar_basic.UarBasicAppinfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 应用appinfo的相关操作
 *
 * @author cpc
 * @create 2018-09-05 16:35
 **/
@Service
@Transactional
public class UarBasicAppinfoServiceImpl implements UarBasicAppinfoService {
    private Logger logger = Logger.getLogger(UarBasicAppinfoServiceImpl.class);

    @Autowired
    private UarBasicAppinfoMapper uarBasicAppinfoMapper;


    /**
     * 根据机构id，查询其下对应的所有网站和移动应用的appaccount(即应用标识at)（返回结果为map<应用中文名，(应用appaccount，当为移动应用时为，Android和ios的appkey)>）
     *
     * @param orgId 机构id
     * @return
     */
    public Map<String, List<String>> findAppaccountListByOrgId(String orgId) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        List<UarBasicAppinfo> appaccountListByUserBasicUserIdList = uarBasicAppinfoMapper.findAppaccountListByOrgId(orgId);
        for (UarBasicAppinfo uarBasicAppinfo : appaccountListByUserBasicUserIdList) {
            List<String> resultAppaccountList = new ArrayList<String>();
            //因为当为移动应用时，可能存在Android和ios的区别，有两个appaccount，所以用list封装
            if (StringUtils.isNotBlank(uarBasicAppinfo.getAppaccount())) {
                resultAppaccountList.add(uarBasicAppinfo.getAppaccount());
            }
            if (StringUtils.isNotBlank(uarBasicAppinfo.getAppaccount2())) {
                resultAppaccountList.add(uarBasicAppinfo.getAppaccount2());
            }
            result.put(uarBasicAppinfo.getAppname(), resultAppaccountList);
        }
        return result;
    }

    /**
     * 根据机构id，查询其下对应的所有网站和移动应用的appaccount(即应用标识at)（返回结果为map<应用中文名，(应用appaccount，当为移动应用时为，Android和ios的appkey)>）
     *
     * @param orgId 机构id
     * @param appType 应用类型(1:网站；2:客户端)
     *
     * @return
     */
    public Map<String, List<String>> findAppaccountListByOrgId(String orgId, Integer appType) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orgId", orgId);
        paramMap.put("appType", appType);
        List<UarBasicAppinfo> appaccountListByUserBasicUserIdList = uarBasicAppinfoMapper.findAppaccountListByOrgIdAndAppType(paramMap);
        for (UarBasicAppinfo uarBasicAppinfo : appaccountListByUserBasicUserIdList) {
            List<String> resultAppaccountList = new ArrayList<String>();
            //因为当为移动应用时，可能存在Android和ios的区别，有两个appaccount，所以用list封装
            if (StringUtils.isNotBlank(uarBasicAppinfo.getAppaccount())) {
                resultAppaccountList.add(uarBasicAppinfo.getAppaccount());
            }
            if (StringUtils.isNotBlank(uarBasicAppinfo.getAppaccount2())) {
                resultAppaccountList.add(uarBasicAppinfo.getAppaccount2());
            }
            result.put(uarBasicAppinfo.getAppname(), resultAppaccountList);
        }
        return result;
    }

    /**
     * 根据应用at，查询应用详情
     *
     * @param appaccount 应用at
     * @return
     */
    public UarBasicAppinfo findAppInfoByAppAccountOrAppAccount2(String appaccount) {
        return uarBasicAppinfoMapper.findAppInfoByAppAccountOrAppAccount2(appaccount);
    }

    /**
     * 获取指定租户下网站或app的所有应用appkey
     *
     * @param lesseeId 租户id
     * @param appType  应用标识1：网站；2：客户端;当为null或""时查询网站和app的所有
     * @return
     */
    public Set<String> getAppKeyByLesseeIdAndAppType(String lesseeId, Integer appType) {
        Set<String> appKeySet = new HashSet<String>();

        //1、根据租户id查询租户下的所有网站的appkey
        Map<String, Object> getAllAppKeyByOrgIdAndAppTypeParamMap = new HashMap<String, Object>();
        getAllAppKeyByOrgIdAndAppTypeParamMap.put("lesseeId", lesseeId);
        getAllAppKeyByOrgIdAndAppTypeParamMap.put("appType", appType);
        List<UarBasicAppinfo> allAppKeyByOrgIdAndAppType = uarBasicAppinfoMapper.getAllAppKeyByOrgIdAndAppType(getAllAppKeyByOrgIdAndAppTypeParamMap);

        for (UarBasicAppinfo appInfo : allAppKeyByOrgIdAndAppType) {
            if (StringUtils.isNotBlank(appInfo.getAppaccount())) {
                appKeySet.add(appInfo.getAppaccount());
            }
            if (StringUtils.isNotBlank(appInfo.getAppaccount2())) {
                appKeySet.add(appInfo.getAppaccount2());
            }
        }

        return appKeySet;
    }
}
