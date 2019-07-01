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

    /**
     * 根据机构id和查询时间，查询接口返回值
     *
     * @param originId   机构id
     * @param searchDate 查询时间
     * @return
     */
    public String findOriginReturnRecordByOriginIdDataLevel(String originId, String searchDate, Integer dataLevel);

    /**
     * 根据机构id和查询时间，查询接口返回值
     *
     * @param originId   机构id
     * @param searchDate 查询时间
     * @return
     */
    public String findOriginReturnRecordByOriginIdAndDataLevel(String originId, String searchDate, Integer dataLevel);

    /**
     * 查询指定机构id的最新一条返回记录
     *
     * @param originId 机构id
     * @return
     */
    public OriginReturnRecord findOriginReturnRecordByOriginIdAndLastDate(String originId);

    /**
     * 查询指定机构id的最新一条返回记录
     *
     * @param originId  机构id
     * @param dataLevel 数据来源(1:mysql;2:画像es)
     * @return
     */
    public OriginReturnRecord findOriginReturnRecordByOriginIdAndLastDateAndDataLevel(String originId, Integer dataLevel);
}
