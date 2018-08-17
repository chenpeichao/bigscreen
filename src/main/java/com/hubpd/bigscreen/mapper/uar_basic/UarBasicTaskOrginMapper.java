package com.hubpd.bigscreen.mapper.uar_basic;

import com.hubpd.bigscreen.bean.uar_basic.UarBasicTaskOrgin;

import java.util.List;

public interface UarBasicTaskOrginMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UarBasicTaskOrgin record);

    int insertSelective(UarBasicTaskOrgin record);

    UarBasicTaskOrgin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UarBasicTaskOrgin record);

    int updateByPrimaryKey(UarBasicTaskOrgin record);

    /**
     * 查询所有的有效(status=1)机构id列表
     *
     * @return
     */
    public List<String> findAllOriginIdList();
}