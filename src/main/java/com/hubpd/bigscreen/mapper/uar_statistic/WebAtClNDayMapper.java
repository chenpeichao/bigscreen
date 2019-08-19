package com.hubpd.bigscreen.mapper.uar_statistic;

import com.hubpd.bigscreen.bean.uar_statistic.WebAtClNDay;
import com.hubpd.bigscreen.bean.uar_statistic.WebAtClNDayKey;

import java.util.Map;
import java.util.Set;

public interface WebAtClNDayMapper {
    int deleteByPrimaryKey(WebAtClNDayKey key);

    int insert(WebAtClNDay record);

    int insertSelective(WebAtClNDay record);

    WebAtClNDay selectByPrimaryKey(WebAtClNDayKey key);

    int updateByPrimaryKeySelective(WebAtClNDay record);

    int updateByPrimaryKey(WebAtClNDay record);

    /**
     * 查询指定appkey集合截止昨日的累计用户数
     *
     * @param appkeSet
     * @return
     */
    public Long getTotalUserByOriginId(Map<String, Object> paramMap);
}