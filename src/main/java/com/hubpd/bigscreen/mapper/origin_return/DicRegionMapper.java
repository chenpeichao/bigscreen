package com.hubpd.bigscreen.mapper.origin_return;

import com.hubpd.bigscreen.bean.origin_return.DicRegion;

import java.util.List;

public interface DicRegionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DicRegion record);

    int insertSelective(DicRegion record);

    DicRegion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DicRegion record);

    int updateByPrimaryKey(DicRegion record);

    /**
     * 查询所有有效的地域名称集合
     *
     * @return
     */
    public List<String> findAllDicRegionName();
}