package com.hubpd.bigscreen.service.weishu_pdmi;

import com.hubpd.bigscreen.vo.WXArticleAnalyseVO;
import com.hubpd.bigscreen.vo.WXUserAnalyseVO;

import java.util.List;

/**
 * weishu中微信分析用户service
 * Created by ceek on 2018-08-08 23:18.
 */
public interface WeiShuPdmiUserService {
    /**
     * 根据用户id列表查询其授权的公众号id列表
     * @param userIdList        用户id列表
     * @param userFollow        公众号与用户关联关系（1:自有，2：关注）
     * @return
     */
    public List<Integer> findPubAccountIdListByUserIdList(List<String> userIdList, Integer userFollow);

    /**
     * 根据公众号id列表查询微信运营列表信息（包括用户统计和点赞数）
     * @param pubAccountIdListByUserIdList        公众号id列表
     * @param beginDateStr                          开始时间
     * @param endDateStr                            结束时间
     * @return
     */
    public List<WXUserAnalyseVO> findUserAnalyseByPubAccountIdListAndSearchDate(List<Integer> pubAccountIdListByUserIdList, String beginDateStr, String endDateStr);

    /**
     * 根据公众号id列表查询微信用户统计信息
     * @param pubAccountIdListByUserIdList        公众号id列表
     * @param beginDateStr                          开始时间
     * @param endDateStr                            结束时间
     * @return
     */
    public List<WXUserAnalyseVO> findUserStatisticsByPubAccountIdListAndSearchDate(List<Integer> pubAccountIdListByUserIdList, String beginDateStr, String endDateStr);

    /**
     * 根据公众号id列表查询微信公众号点赞信息
     * @param pubAccountIdListByUserIdList        公众号id列表
     * @param beginDateStr                          开始时间
     * @param endDateStr                            结束时间
     * @return
     */
    public List<WXUserAnalyseVO> findPubAccountLikeNumByPubAccountIdListAndSearchDate(List<Integer> pubAccountIdListByUserIdList, String beginDateStr, String endDateStr);

    /**
     *  根据公众号id列表，查询文章信息以及指定日期段的阅读数和点赞数
     * @param pubAccountIdListByUserIdList        公众号id列表
     * @param beginDateStr                          开始时间
     * @param endDateStr                            结束时间
     * @return
     */
    public List<WXArticleAnalyseVO> findgetWXArticleStatByAccountIdListAndSearchDate(List<Integer> pubAccountIdListByUserIdList, String beginDateStr, String endDateStr);
}
