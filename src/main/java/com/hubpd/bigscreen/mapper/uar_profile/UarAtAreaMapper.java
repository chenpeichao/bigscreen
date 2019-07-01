package com.hubpd.bigscreen.mapper.uar_profile;

import com.hubpd.bigscreen.bean.uar_profile.UarAtArea;
import com.hubpd.bigscreen.dto.UserAreaCountDTO;

import java.util.List;
import java.util.Map;

public interface UarAtAreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UarAtArea record);

    int insertSelective(UarAtArea record);

    UarAtArea selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UarAtArea record);

    int updateByPrimaryKey(UarAtArea record);

    public List<UserAreaCountDTO> findUserCountInProvinceByAppKey(Map<String, Object> paramMap);
}