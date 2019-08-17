package com.hubpd.bigscreen.service.origin_return.impl;

import com.hubpd.bigscreen.bean.uar_profile.OriginReturnRecordAllProvinceRegion;
import com.hubpd.bigscreen.mapper.origin_return.OriginReturnRecordAllProvinceRegionMapper;
import com.hubpd.bigscreen.service.origin_return.OriginReturnRecordAllProvinceRegionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ceek
 * @create 2019-08-15 11:27
 **/
@Service
public class OriginReturnRecordAllProvinceRegionServiceImpl implements OriginReturnRecordAllProvinceRegionService {
    private Logger logger = Logger.getLogger(OriginReturnRecordAllProvinceRegionServiceImpl.class);

    @Autowired
    private OriginReturnRecordAllProvinceRegionMapper originReturnRecordAllProvinceRegionMapper;

    /**
     * @return
     */
    public void insert(OriginReturnRecordAllProvinceRegion originReturnRecordAllProvinceRegion) {
        originReturnRecordAllProvinceRegionMapper.insert(originReturnRecordAllProvinceRegion);
    }

    /**
     * 根据机构id和查询时间，查询接口返回值
     *
     * @param originId   机构id
     * @param searchDate 查询时间
     * @return
     */
    public String findReturnRecordByOriginId(String originId, String searchDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("originId", originId);
        params.put("searchDate", searchDate);
        //查询最近的一条数据
        List<String> originReturnRecordByOriginIdList = originReturnRecordAllProvinceRegionMapper.findReturnRecordByOriginId(params);
        if (originReturnRecordByOriginIdList != null && originReturnRecordByOriginIdList.size() > 0) {
            if (originReturnRecordByOriginIdList.size() > 1) {
                logger.error("机构id【" + originId + "】在接口用户画像全省份缓存数据库中保存的接口返回信息保存记录错误，记录数大于1条");
            }
            return originReturnRecordByOriginIdList.get(0);
        } else {
            return "";
        }
    }
}
