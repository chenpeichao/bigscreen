package com.hubpd.bigscreen.service.origin_return;

import com.hubpd.bigscreen.bean.origin_return.OriginReturnRecord;

import java.util.Date;

/**
 * 大屏接口调用记录
 *
 * @author cpc
 * @create 2018-08-15 18:55
 **/
public interface OriginReturnRecordService {
    public int insert(OriginReturnRecord record);

    public int insertSelective(OriginReturnRecord record);

    /**
     * 根据机构id和查询时间，查询接口返回值
     *
     * @param originId   机构id
     * @param searchDate 查询时间
     * @return
     */
    public String findOriginReturnRecordByOriginId(String originId, String searchDate);
}
