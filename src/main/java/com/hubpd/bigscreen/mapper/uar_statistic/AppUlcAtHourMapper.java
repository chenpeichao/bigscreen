package com.hubpd.bigscreen.mapper.uar_statistic;

import com.hubpd.bigscreen.bean.uar_statistic.AppUlcAtHour;
import com.hubpd.bigscreen.bean.uar_statistic.AppUlcAtHourKey;

public interface AppUlcAtHourMapper {
    int deleteByPrimaryKey(AppUlcAtHourKey key);

    int insert(AppUlcAtHour record);

    int insertSelective(AppUlcAtHour record);

    AppUlcAtHour selectByPrimaryKey(AppUlcAtHourKey key);

    int updateByPrimaryKeySelective(AppUlcAtHour record);

    int updateByPrimaryKey(AppUlcAtHour record);
}