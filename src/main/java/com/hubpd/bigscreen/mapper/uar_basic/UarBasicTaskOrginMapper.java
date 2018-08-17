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
     * 查询所有的有效(status=1)机构id列表---大屏缓存
     *
     * @return
     */
    public List<String> findAllOriginIdListInBigscreen();

    /**
     * 根据机构id查询大屏缓存的机构信息
     *
     * @param orginId 机构id
     * @return
     */
    public List<UarBasicTaskOrgin> findTaskOriginByOriginIdInBigscreen(String orginId);
}