package com.hubpd.bigscreen.mapper.weishu_pdmi;

import com.hubpd.bigscreen.bean.weishu_pdmi.PubAccountUserRelation;
import com.hubpd.bigscreen.vo.WXUserAnalyseVO;

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

    /**
     * 根据公众号id列表查询微信运营列表信息
     * @param params            公众号id列表,开始时间,结束时间
     * @return
     */
    public List<WXUserAnalyseVO> findUserAnalyseByPubAccountIdListAndSearchDate(Map<String, Object> params);

    /**
     * 根据公众号id列表查询微信用户统计信息
     * @param params            公众号id列表,开始时间,结束时间
     * @return
     */
    public List<WXUserAnalyseVO> findUserStatisticsByPubAccountIdListAndSearchDate(Map<String, Object> params);

    /**
     * 根据公众号id列表查询微信公众号点赞信息
     * @param params            公众号id列表,开始时间,结束时间
     * @return
     */
    public List<WXUserAnalyseVO> findPubAccountLikeNumByPubAccountIdListAndSearchDate(Map<String, Object> params);

}