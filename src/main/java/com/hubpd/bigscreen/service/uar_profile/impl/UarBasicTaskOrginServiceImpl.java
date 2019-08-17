package com.hubpd.bigscreen.service.uar_profile.impl;

import com.hubpd.bigscreen.mapper.uar_basic.UarBasicTaskOrginMapper;
import com.hubpd.bigscreen.service.uar_profile.UarBasicTaskOrginService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * uar_basic中配置的需要缓存的机构信息实现类
 *
 * @author ceek
 * @create 2019-08-15 11:13
 **/
@Service
public class UarBasicTaskOrginServiceImpl implements UarBasicTaskOrginService {
    private Logger logger = Logger.getLogger(UarBasicTaskOrginServiceImpl.class);

    @Autowired
    private UarBasicTaskOrginMapper uarBasicTaskOrginMapper;

    /**
     * 查询所有的机构信息---大屏缓存
     *
     * @return
     */
    public List<String> findAllOriginIdListInBigscreen() {
        return uarBasicTaskOrginMapper.findAllOriginIdListInBigscreen();
    }
}
