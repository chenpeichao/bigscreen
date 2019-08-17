package com.hubpd.bigscreen.mapper.weishu_pdmi;


import com.github.pagehelper.Page;
import com.hubpd.bigscreen.bean.weishu_pdmi.PubRank;
import com.hubpd.bigscreen.dto.PubRankDTO;

import java.util.Map;

public interface PubRankMapper {
    int insert(PubRank record);

    int insertSelective(PubRank record);

    /**
     * 根据公众号id集合以及日期类型，排序字段，排序标识查询公众号排行榜单列表
     *
     * @param pubAccountIdList 公众号id集合
     * @param dayType          日期类型(7/30)
     * @param sortName         排序字段
     * @param sortBy           排序标识(asc/desc)
     * @return
     */
    public Page<PubRankDTO> findPubRankByPubIdListAndParam(Map<String, Object> paramMap);
}