package com.hubpd.bigscreen.mapper.origin_return;

import com.hubpd.bigscreen.bean.origin_return.OriginReturnRecord;

import java.util.List;
import java.util.Map;

public interface OriginReturnRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OriginReturnRecord record);

    int insertSelective(OriginReturnRecord record);

    OriginReturnRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OriginReturnRecord record);

    int updateByPrimaryKeyWithBLOBs(OriginReturnRecord record);

    int updateByPrimaryKey(OriginReturnRecord record);

    /**
     * 根据机构id和查询时间，查询接口返回值
     *
     * @param params 机构id，查询时间
     * @return
     */
    public List<String> findOriginReturnRecordByOriginId(Map<String, Object> params);

    /**
     * 根据机构id和查询时间，查询接口返回值
     *
     * @param params 机构id，查询时间
     * @return
     */
    public List<String> findOriginReturnRecordByOriginIdAndDataLevel(Map<String, Object> params);

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
     * @param paramMap 机构id
     * @return
     */
    public OriginReturnRecord findOriginReturnRecordByOriginIdAndLastDateAndDataLevel(Map<String, Object> paramMap);
}