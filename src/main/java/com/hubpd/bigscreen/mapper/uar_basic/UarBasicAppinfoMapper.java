package com.hubpd.bigscreen.mapper.uar_basic;

import com.hubpd.bigscreen.bean.uar_basic.UarBasicAppinfo;

public interface UarBasicAppinfoMapper {
    int deleteByPrimaryKey(Integer appid);

    int insert(UarBasicAppinfo record);

    int insertSelective(UarBasicAppinfo record);

    UarBasicAppinfo selectByPrimaryKey(Integer appid);

    int updateByPrimaryKeySelective(UarBasicAppinfo record);

    int updateByPrimaryKeyWithBLOBs(UarBasicAppinfo record);

    int updateByPrimaryKey(UarBasicAppinfo record);
}