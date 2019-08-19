package com.hubpd.bigscreen.mapper.uar_statistic;

import com.hubpd.bigscreen.bean.uar_statistic.AppUulcAtHour;
import com.hubpd.bigscreen.bean.uar_statistic.AppUulcAtHourKey;

public interface AppUulcAtHourMapper {
    int deleteByPrimaryKey(AppUulcAtHourKey key);

    int insert(AppUulcAtHour record);

    int insertSelective(AppUulcAtHour record);

    AppUulcAtHour selectByPrimaryKey(AppUulcAtHourKey key);

    int updateByPrimaryKeySelective(AppUulcAtHour record);

    int updateByPrimaryKey(AppUulcAtHour record);
}