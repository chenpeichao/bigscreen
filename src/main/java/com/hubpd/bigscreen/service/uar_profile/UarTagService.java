package com.hubpd.bigscreen.service.uar_profile;

/**
 * 用户标签Service
 *
 * @author ceek
 * @create 2019-08-19 13:11
 **/
public interface UarTagService {
    /**
     * 根据标签id查询标签名称
     *
     * @param tagId 标签id
     * @return
     */
    public String getTagNameByTagId(Integer tagId);
}
