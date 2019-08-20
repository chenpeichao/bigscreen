package com.hubpd.bigscreen.mapper.uar_profile;

import com.hubpd.bigscreen.bean.uar_profile.UarTag;

public interface UarTagMapper {
    int deleteByPrimaryKey(Integer tagId);

    int insert(UarTag record);

    int insertSelective(UarTag record);

    UarTag selectByPrimaryKey(Integer tagId);

    int updateByPrimaryKeySelective(UarTag record);

    int updateByPrimaryKey(UarTag record);

    /**
     * 根据标签id查询标签名称
     *
     * @param tagId 标签id
     * @return
     */
    public String getTagNameByTagId(Integer tagId);
}