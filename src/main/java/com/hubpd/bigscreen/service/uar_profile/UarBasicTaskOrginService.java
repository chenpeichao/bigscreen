package com.hubpd.bigscreen.service.uar_profile;

import java.util.List;

/**
 * uar_basic中配置的需要缓存的机构信息
 *
 * @author ceek
 * @create 2019-08-15 11:12
 **/
public interface UarBasicTaskOrginService {
    /**
     * 查询所有的机构信息---大屏缓存
     *
     * @return
     */
    public List<String> findAllOriginIdListInBigscreen();
}
