package com.hubpd.bigscreen.service.uar_basic.impl;

import com.hubpd.bigscreen.bean.uar_basic.Media;
import com.hubpd.bigscreen.mapper.uar_basic.MediaMapper;
import com.hubpd.bigscreen.service.uar_basic.MediaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * uar机构service
 *
 * @author cpc
 * @create 2019-04-09 19:23
 **/
@Service
public class MediaServiceImpl implements MediaService {
    private Logger logger = Logger.getLogger(MediaServiceImpl.class);

    @Autowired
    private MediaMapper mediaMapper;

    /**
     * 查询指定系统类型的机构id集合
     *
     * @param sysType 系统类型1：uar；2：甘肃
     * @return
     */
    public Set<String> findAllOriginIdListInBigscreen(Integer sysType) {
        return mediaMapper.findAllOriginIdListInBigscreen(sysType);
    }

    /**
     * 根据系统类型和机构id查询是否存在此机构
     *
     * @param orgId
     * @param sysType
     * @return
     */
    public Media findMediaByOrgIdAndSystype(String orgId, Integer sysType) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orgId", orgId);
        paramMap.put("sysType", sysType);
        return mediaMapper.findMediaByOrgIdAndSystype(paramMap);
    }

    /**
     * 根据系统类型和机构id查询是否存在此机构
     *
     * @param orgId
     * @return
     */
    public Media findMediaByOrgId(String orgId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orgId", orgId);
        return mediaMapper.findMediaByOrgId(paramMap);
    }
}
