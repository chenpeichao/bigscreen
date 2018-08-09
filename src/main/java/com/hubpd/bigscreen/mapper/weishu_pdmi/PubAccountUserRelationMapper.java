package com.hubpd.bigscreen.mapper.weishu_pdmi;

import com.hubpd.bigscreen.bean.weishu_pdmi.PubAccountUserRelation;

import java.util.List;
import java.util.Map;

public interface PubAccountUserRelationMapper {
    int insert(PubAccountUserRelation record);

    int insertSelective(PubAccountUserRelation record);

    /**
     * 根据用户id列表查询其授权的公众号id列表
     * @param params            用户id列表,公众号与用户关联关系（1:自有，2：关注）
     * @return
     */
    public List<Integer> findPubAccountIdListByUserIdList(Map<String, Object> params);
}