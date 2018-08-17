package com.hubpd.bigscreen.service.origin_return.impl;

import com.hubpd.bigscreen.mapper.origin_return.DicRegionMapper;
import com.hubpd.bigscreen.service.origin_return.DicRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地域字典
 *
 * @author cpc
 * @create 2018-08-16 19:37
 **/
@Service
public class DicRegionServiceImpl implements DicRegionService {
    @Autowired
    private DicRegionMapper dicRegionMapper;

    /**
     * 查询所有有效的地域名称集合
     *
     * @return
     */
    public List<String> findAllDicRegionName() {
        return dicRegionMapper.findAllDicRegionName();
    }
}
