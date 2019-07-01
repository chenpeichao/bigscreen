package com.hubpd.bigscreen.service.origin_return.impl;

import com.hubpd.bigscreen.bean.origin_return.OriginReturnRecord;
import com.hubpd.bigscreen.mapper.origin_return.OriginReturnRecordMapper;
import com.hubpd.bigscreen.service.origin_return.OriginReturnRecordService;
import com.hubpd.bigscreen.service.uar_profile.impl.UserAnalyseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大屏接口调用记录
 *
 * @author cpc
 * @create 2018-08-15 18:56
 **/
@Service
@Transactional
public class OriginReturnRecordServiceImpl implements OriginReturnRecordService {
    private Logger logger = Logger.getLogger(OriginReturnRecordServiceImpl.class);

    @Autowired
    private OriginReturnRecordMapper originReturnRecordMapper;

    public int insert(OriginReturnRecord record) {
        return originReturnRecordMapper.insert(record);
    }

    public int insertSelective(OriginReturnRecord record) {
        return originReturnRecordMapper.insertSelective(record);
    }

    /**
     * 根据机构id和查询时间，查询接口返回值
     *
     * @param originId   机构id
     * @param searchDate 查询时间
     * @return
     */
    public String findOriginReturnRecordByOriginId(String originId, String searchDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("originId", originId);
        params.put("searchDate", searchDate);
        List<String> originReturnRecordByOriginIdList = originReturnRecordMapper.findOriginReturnRecordByOriginId(params);
        if (originReturnRecordByOriginIdList != null && originReturnRecordByOriginIdList.size() > 0) {
            if (originReturnRecordByOriginIdList.size() > 1) {
                logger.error("机构id【" + originId + "】在接口缓存数据库中保存的接口返回信息保存记录错误，记录数大于1条");
            }
            return originReturnRecordByOriginIdList.get(0);
        } else {
            return "";
        }
    }

    /**
     * 根据机构id和查询时间，查询接口返回值
     *
     * @param originId   机构id
     * @param searchDate 查询时间
     * @return
     */
    public String findOriginReturnRecordByOriginIdDataLevel(String originId, String searchDate, Integer dataLevel) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("originId", originId);
        params.put("searchDate", searchDate);
        params.put("dataLevel", dataLevel);
        List<String> originReturnRecordByOriginIdList = originReturnRecordMapper.findOriginReturnRecordByOriginIdAndDataLevel(params);
        if (originReturnRecordByOriginIdList != null && originReturnRecordByOriginIdList.size() > 0) {
            if (originReturnRecordByOriginIdList.size() > 1) {
                logger.error("机构id【" + originId + "】在接口缓存数据库中保存的接口返回信息保存记录错误，记录数大于1条");
            }
            return originReturnRecordByOriginIdList.get(0);
        } else {
            return "";
        }
    }

    /**
     * 根据机构id和查询时间，查询接口返回值
     *
     * @param originId   机构id
     * @param searchDate 查询时间
     * @return
     */
    public String findOriginReturnRecordByOriginIdAndDataLevel(String originId, String searchDate, Integer dataLevel) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("originId", originId);
        params.put("searchDate", searchDate);
        params.put("dataLevel", dataLevel);
        List<String> originReturnRecordByOriginIdList = originReturnRecordMapper.findOriginReturnRecordByOriginIdAndDataLevel(params);
        if (originReturnRecordByOriginIdList != null && originReturnRecordByOriginIdList.size() > 0) {
            if (originReturnRecordByOriginIdList.size() > 1) {
                logger.error("机构id【" + originId + "】在接口缓存数据库中保存的接口返回信息保存记录错误，记录数大于1条");
            }
            return originReturnRecordByOriginIdList.get(0);
        } else {
            return "";
        }
    }

    /**
     * 查询指定机构id的最新一条返回记录
     *
     * @param originId 机构id
     * @return
     */
    public OriginReturnRecord findOriginReturnRecordByOriginIdAndLastDate(String originId) {
        return originReturnRecordMapper.findOriginReturnRecordByOriginIdAndLastDate(originId);
    }

    /**
     * 查询指定机构id的最新一条返回记录
     *
     * @param originId  机构id
     * @param dataLevel 数据来源(1:mysql;2:画像es)
     * @return
     */
    public OriginReturnRecord findOriginReturnRecordByOriginIdAndLastDateAndDataLevel(String originId, Integer dataLevel) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("originId", originId);
        paramMap.put("dataLevel", dataLevel);
        return originReturnRecordMapper.findOriginReturnRecordByOriginIdAndLastDateAndDataLevel(paramMap);
    }
}
