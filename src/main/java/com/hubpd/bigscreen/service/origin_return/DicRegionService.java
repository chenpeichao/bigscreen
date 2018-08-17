package com.hubpd.bigscreen.service.origin_return;

import java.util.List;

/**
 * 地域字典表
 *
 * @author cpc
 * @create 2018-08-16 19:36
 **/
public interface DicRegionService {
    /**
     * 查询所有有效的地域名称集合
     *
     * @return
     */
    public List<String> findAllDicRegionName();
}
