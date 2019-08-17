package com.hubpd.bigscreen.service.origin_return;


import com.hubpd.bigscreen.bean.uar_profile.OriginReturnRecordAllProvinceRegion;

/**
 * 用户画像全省份记录Service
 *
 * @author ceek
 * @create 2019-08-15 11:26
 **/
public interface OriginReturnRecordAllProvinceRegionService {
    /**
     * @return
     */
    public void insert(OriginReturnRecordAllProvinceRegion originReturnRecordAllProvinceRegion);

    /**
     * 根据机构id和查询时间，查询接口返回值
     *
     * @param originId   机构id
     * @param searchDate 查询时间
     * @return
     */
    public String findReturnRecordByOriginId(String originId, String searchDate);
}
