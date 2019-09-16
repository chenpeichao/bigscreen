package com.hubpd.bigscreen.mapper.uar_statistic;

import com.hubpd.bigscreen.bean.uar_statistic.AppAtDay;
import com.hubpd.bigscreen.bean.uar_statistic.AppAtDayKey;
import com.hubpd.bigscreen.dto.AppAtDayDTO;

import java.util.Map;
import java.util.Set;

public interface AppAtDayMapper {
    int deleteByPrimaryKey(AppAtDayKey key);

    int insert(AppAtDay record);

    int insertSelective(AppAtDay record);

    AppAtDay selectByPrimaryKey(AppAtDayKey key);

    int updateByPrimaryKeySelective(AppAtDay record);

    int updateByPrimaryKey(AppAtDay record);

    /**
     * 获取指定appkey的应用在指定时间的新增和活跃用户
     *
     * @param appKeySet 客户端appkey集合
     * @param searchDay 查询日期
     * @return
     */
    public AppAtDayDTO getNewUVAndActiveUVByAppKeySetAndDay(Map<String, Object> paramMap);
}