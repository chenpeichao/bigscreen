package com.hubpd.bigscreen.service.weishu_pdmi.impl;

import com.hubpd.bigscreen.mapper.weishu_pdmi.PubAccountUserRelationMapper;
import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import com.hubpd.bigscreen.service.weishu_pdmi.WeiShuPdmiUserService;
import com.hubpd.bigscreen.vo.WXUserAnalyseVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * weishu中微信分析用户service
 * Created by ceek on 2018-08-08 23:19.
 */
@Service
@Transactional
public class WeiShuPdmiUserServiceImpl implements WeiShuPdmiUserService {
    private Logger logger = Logger.getLogger(WeiShuPdmiUserServiceImpl.class);

    /** 微信数据库中用户和公众号关联关系 */
    @Autowired
    private PubAccountUserRelationMapper pubAccountUserRelationMapper;

    /**
     * 根据用户id列表查询其授权的公众号id列表
     * @param userIdList        用户id列表
     * @param userFollow        公众号与用户关联关系（1:自有，2：关注）
     * @return
     */
    public List<Integer> findPubAccountIdListByUserIdList(List<String> userIdList, Integer userFollow) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userIdList", userIdList);
        params.put("userFollow", userFollow);
        return pubAccountUserRelationMapper.findPubAccountIdListByUserIdList(params);
    }

    /**
     * 根据公众号id列表查询微信运营列表信息
     * @param pubAccountIdListByUserIdList        公众号id列表
     * @param beginDateStr                          开始时间
     * @param endDateStr                            结束时间
     * @return
     */
    public List<WXUserAnalyseVO> findUserAnalyseByPubAccountIdListAndSearchDate(List<Integer> pubAccountIdListByUserIdList, String beginDateStr, String endDateStr) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pubAccountIdListByUserIdList", pubAccountIdListByUserIdList);
        params.put("beginDateStr", beginDateStr);
        params.put("endDateStr", endDateStr);
        return pubAccountUserRelationMapper.findUserAnalyseByPubAccountIdListAndSearchDate(params);
    }
}
