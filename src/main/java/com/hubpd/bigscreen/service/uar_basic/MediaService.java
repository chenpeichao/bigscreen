package com.hubpd.bigscreen.service.uar_basic;


import com.hubpd.bigscreen.bean.uar_basic.Media;

import java.util.Set;

/**
 * uar机构service
 *
 * @author cpc
 * @create 2019-04-09 19:22
 **/
public interface MediaService {
    /**
     * 查询指定系统类型的机构id集合
     *
     * @param sysType 系统类型1：uar；2：甘肃
     * @return
     */
    public Set<String> findAllOriginIdListInBigscreen(Integer sysType);

    /**
     * 根据系统类型和机构id查询是否存在此机构
     *
     * @param orgId
     * @param sysType
     * @return
     */
    public Media findMediaByOrgIdAndSystype(String orgId, Integer sysType);

    /**
     * 根据系统类型和机构id查询是否存在此机构
     *
     * @param orgId
     * @return
     */
    public Media findMediaByOrgId(String orgId);
}
