package com.hubpd.bigscreen.mapper.uar_statistic;

import com.hubpd.bigscreen.bean.uar_statistic.WebAtClNHour;
import com.hubpd.bigscreen.bean.uar_statistic.WebAtClNHourKey;

import java.util.Map;
import java.util.Set;

public interface WebAtClNHourMapper {
    int deleteByPrimaryKey(WebAtClNHourKey key);

    int insert(WebAtClNHour record);

    int insertSelective(WebAtClNHour record);

    WebAtClNHour selectByPrimaryKey(WebAtClNHourKey key);

    int updateByPrimaryKeySelective(WebAtClNHour record);

    int updateByPrimaryKey(WebAtClNHour record);

    /**
     * 查询指定appkey集合查询天昨日的累计用户数
     *
     * @param appkeSet   应用集合
     * @param searchDate 查询时间
     * @return
     */
    public Long getSumUVByAppkeySet(Map<String, Object> paramMap);
}