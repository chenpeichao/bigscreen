package com.hubpd.bigscreen.service.weishu_pdmi.impl;


import com.github.pagehelper.Page;
import com.hubpd.bigscreen.dto.PubRankDTO;
import com.hubpd.bigscreen.mapper.weishu_pdmi.PubRankMapper;
import com.hubpd.bigscreen.service.weishu_pdmi.PubRankService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公众号榜单service实现类
 *
 * @author ceek
 * @create 2019-08-17 13:21
 **/
@Service
public class PubRankServiceImpl implements PubRankService {
    private Logger logger = Logger.getLogger(PubRankServiceImpl.class);

    @Autowired
    private PubRankMapper pubRankMapper;

    /**
     * 根据公众号id集合以及日期类型，排序字段，排序标识查询公众号排行榜单列表
     *
     * @param pubAccountIdList 公众号id集合
     * @param dayType          日期类型(7/30)
     * @param sortName         排序字段
     * @param sortBy           排序标识(asc/desc)
     * @return
     */
    public Page<PubRankDTO> findPubRankByPubIdListAndParam(List<Integer> pubAccountIdList, Integer dayType, String sortName, String sortBy) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pubAccountIdList", pubAccountIdList);
        paramMap.put("dayType", dayType);
        paramMap.put("sortName", sortName);
        paramMap.put("sortBy", sortBy);
        return pubRankMapper.findPubRankByPubIdListAndParam(paramMap);
    }
}
