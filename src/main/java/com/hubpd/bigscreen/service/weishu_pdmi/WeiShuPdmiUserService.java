package com.hubpd.bigscreen.service.weishu_pdmi;

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
     * 根据公众号id列表查询微信运营列表信息
     * @param pubAccountIdListByUserIdList        公众号id列表
     * @param beginDateStr                          开始时间
     * @param endDateStr                            结束时间
     * @return
     */
    public List<WXUserAnalyseVO> findUserAnalyseByPubAccountIdListAndSearchDate(List<Integer> pubAccountIdListByUserIdList, String beginDateStr, String endDateStr);
}
