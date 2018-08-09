package com.hubpd.bigscreen.mapper.uar_basic;

import com.hubpd.bigscreen.bean.uar_basic.UarBasicUser;

import java.util.List;

public interface UarBasicUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(UarBasicUser record);

    int insertSelective(UarBasicUser record);

    UarBasicUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UarBasicUser record);

    int updateByPrimaryKey(UarBasicUser record);

    /**
     * 根据机构id，查询uar系统中指定机构下所有用户信息
     * @param orgId         机构id
     * @return
     */
    public List<UarBasicUser> findUarBasicUserListByOrginId(String orgId);

    /**
     * 根据机构id，查询uar系统中指定机构下所有用户id列表信息
     * @param orginId         机构id
     * @return
     */
    public List<String> findUarBasicUserIdListByOrginId(String orginId);
}