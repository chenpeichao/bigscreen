package com.hubpd.bigscreen.mapper.uar_basic;

import com.hubpd.bigscreen.bean.uar_basic.UarBasicAppinfo;

import java.util.List;
import java.util.Map;

public interface UarBasicAppinfoMapper {
    int deleteByPrimaryKey(Integer appid);

    int insert(UarBasicAppinfo record);

    int insertSelective(UarBasicAppinfo record);

    UarBasicAppinfo selectByPrimaryKey(Integer appid);

    int updateByPrimaryKeySelective(UarBasicAppinfo record);

    int updateByPrimaryKeyWithBLOBs(UarBasicAppinfo record);

    int updateByPrimaryKey(UarBasicAppinfo record);

    /**
     * 根据机构id，查询其下对应的所有网站和移动应用的appaccount
     *
     * @param orgId 机构id
     * @return
     */
    public List<UarBasicAppinfo> findAppaccountListByOrgId(String orgId);

    /**
     * 根据应用at，查询应用详情
     *
     * @param appaccount 应用at
     * @return
     */
    public UarBasicAppinfo findAppInfoByAppAccountOrAppAccount2(String appaccount);
}