package com.hubpd.bigscreen.mapper.uar_basic;

import com.hubpd.bigscreen.bean.uar_basic.UarBasicAppinfo;
import com.hubpd.bigscreen.bean.uar_basic.UarBasicUserApp;

import java.util.List;
import java.util.Map;

public interface UarBasicUserAppMapper {
    int insert(UarBasicUserApp record);

    int insertSelective(UarBasicUserApp record);

    /**
     * 根据用户id列表，查询用户下对应的所有网站和移动应用的appaccount(即应用标识at)
     *
     * @param params 用户id列表
     * @return
     */
    public List<UarBasicAppinfo> findAppaccountListByUserBasicUserIdList(Map<String, Object> params);
}