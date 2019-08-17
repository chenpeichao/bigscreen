package com.hubpd.bigscreen.mapper.origin_return;


import com.hubpd.bigscreen.bean.uar_profile.OriginReturnRecordAllProvinceRegion;

import java.util.List;
import java.util.Map;

public interface OriginReturnRecordAllProvinceRegionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OriginReturnRecordAllProvinceRegion record);

    int insertSelective(OriginReturnRecordAllProvinceRegion record);

    OriginReturnRecordAllProvinceRegion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OriginReturnRecordAllProvinceRegion record);

    int updateByPrimaryKeyWithBLOBs(OriginReturnRecordAllProvinceRegion record);

    int updateByPrimaryKey(OriginReturnRecordAllProvinceRegion record);

    /**
     * 根据机构id和查询时间，查询接口返回值
     *
     * @param paramsMap 机构id，查询时间
     * @return
     */
    public List<String> findReturnRecordByOriginId(Map<String, Object> paramsMap);
}