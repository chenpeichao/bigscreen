package com.hubpd.bigscreen.service.uar_basic.impl;

import com.hubpd.bigscreen.bean.uar_basic.UarBasicAppinfo;
import com.hubpd.bigscreen.mapper.uar_basic.UarBasicAppinfoMapper;
import com.hubpd.bigscreen.service.uar_basic.UarBasicAppinfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
