package com.hubpd.bigscreen.service.uar_profile.impl;

import com.hubpd.bigscreen.mapper.uar_profile.UarTagMapper;
import com.hubpd.bigscreen.service.uar_profile.UarTagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户标签Service实现类
 *
 * @author ceek
 * @create 2019-08-19 13:11
 **/
@Service
public class UarTagServiceImpl implements UarTagService {
    private Logger logger = Logger.getLogger(UarTagServiceImpl.class);

    @Autowired
    private UarTagMapper uarTagMapper;

    /**
     * 根据标签id查询标签名称
     *
     * @param tagId 标签id
     * @return
     */
    public String getTagNameByTagId(Integer tagId) {
        return uarTagMapper.getTagNameByTagId(tagId);
    }
}
