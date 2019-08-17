package com.hubpd.bigscreen.service.uar_profile.impl;

import com.alibaba.fastjson.JSON;
import com.hubpd.bigscreen.bean.origin_return.OriginReturnRecord;
import com.hubpd.bigscreen.bean.uar_basic.UarBasicTaskOrgin;
import com.hubpd.bigscreen.bean.uar_profile.OriginReturnRecordAllProvinceRegion;
import com.hubpd.bigscreen.service.common.TaskGetUserAnalyseService;
import com.hubpd.bigscreen.service.origin_return.OriginReturnRecordAllProvinceRegionService;
import com.hubpd.bigscreen.service.origin_return.OriginReturnRecordService;
import com.hubpd.bigscreen.service.uar_basic.UarBasicAppinfoService;
import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import com.hubpd.bigscreen.service.uar_profile.UarBasicTaskOrginService;
import com.hubpd.bigscreen.service.uar_profile.UserAnalyseService;
import com.hubpd.bigscreen.utils.*;
import com.hubpd.bigscreen.vo.UserAnalyseRegionVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * 用户分析--性别，年龄，地域
 * @author cpc
 * @create 2018-08-11 20:07
 **/
@Service
public class UserAnalyseServiceImpl implements UserAnalyseService {
    private Logger logger = Logger.getLogger(UserAnalyseServiceImpl.class);

    @Autowired
    private UarBasicUserService uarBasicUserService;
    @Autowired
    private OriginReturnRecordService originReturnRecordService;
    @Autowired
    private TaskGetUserAnalyseService taskGetUserAnalyseService;
    @Autowired
    private OriginReturnRecordAllProvinceRegionService originReturnRecordAllProvinceRegionService;

    @Autowired
    private UarBasicTaskOrginService uarBasicTaskOrginService;
    @Autowired
    private UarBasicAppinfoService uarBasicAppinfoService;

    /**
     * 用户分析接口，计算性别，青老中，前5地域
     *
     * @param orginId 机构id
     * @return
     */
    public Map<String, Object> getUserAnalyseReturnData(String orginId, Integer dataLevel) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String currentDateStr = DateUtils.getDateStrByDate(new Date(), "yyyy-MM-dd");

        //1、首先查询传递的机构id，是否在uar中是有效的机构id
        List<String> allOriginIdListInUar = uarBasicUserService.findAllOriginIdListInUar();
        if (!allOriginIdListInUar.contains(orginId)) {
            //传递的机构id在uar中不存在时，返回null数据
            resultMap.put("code", 0);
            resultMap.put("message", "系统中不存在此机构id");
            return resultMap;
        }

        // 2、首先从mysql数据库中查询指定机构id的最新一条返回记录
        OriginReturnRecord originReturnRecordDB = originReturnRecordService.findOriginReturnRecordByOriginIdAndLastDateAndDataLevel(orginId, dataLevel);
        if (originReturnRecordDB != null) {
            resultMap.put("code", 1);
            resultMap.put("data", JSON.parse(originReturnRecordDB.getReturnJson()));
            return resultMap;
        } else {
            // 当mysql中没有此机构id下的数据时
            //1、向机构id大屏缓存表里面添加此机构id，
            //1.1、首先查询机构id在大屏机构表里面有没有存储
            List<UarBasicTaskOrgin> taskOriginByOriginIdInBigscreenList = uarBasicUserService.findTaskOriginByOriginIdInBigscreen(orginId);
            if (taskOriginByOriginIdInBigscreenList != null && taskOriginByOriginIdInBigscreenList.size() > 0) {
                // 当大屏机构表里存在此机构id对应信息，启动新线程执行接口调用
                taskGetUserAnalyseService.getUserAnalyse(orginId, dataLevel);
                resultMap.put("code", 0);
                resultMap.put("message", "由于此机构【" + currentDateStr + "】的数据未抓取，请1小时后再试！！");
                return resultMap;
            } else {
                resultMap.put("code", 0);
                resultMap.put("message", "未授权的机构id，请联系管理员授权！！");
                return resultMap;
            }
        }
    }

    /**
     * 用户分析接口，计算性别，青老中，全省份地域
     *
     * @param orginId 机构id
     * @return
     */
    public Map<String, Object> getUserAnalyseAllRegion(String orginId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<String> allOriginIdListInBigscreen = uarBasicTaskOrginService.findAllOriginIdListInBigscreen();
        if (null == allOriginIdListInBigscreen || allOriginIdListInBigscreen.size() == 0 || !allOriginIdListInBigscreen.contains(orginId.trim())) {
            resultMap.put("code", ErrorCode.ERROR_CODE_NOT_FOUND_MEDIA_ID);
            resultMap.put("message", "系统中不存在指定机构,请联系管理员添加");
            return resultMap;
        }

        String currentDateStr = DateUtils.getDateStrByDate(new Date(), "yyyy-MM-dd");

        // 首先从mysql数据库中查询数据，如果有数据，则直接返回，没有则进行es计算返回，并保存查询天和机构的数据到mysql数据库缓存
        String originReturnRecordStr = originReturnRecordAllProvinceRegionService.findReturnRecordByOriginId(orginId, currentDateStr);
        if (StringUtils.isNotBlank(originReturnRecordStr)) {
            resultMap.put("code", 0);
            resultMap.put("data", JSON.parse(originReturnRecordStr));
            return resultMap;
        }


        // 对于指定机构在指定查询日期的返回数据，进行mysql数据库的缓存
        String returnDate = originReturnRecordService.findOriginReturnRecordByOriginId(orginId, currentDateStr);

        if (StringUtils.isNotBlank(returnDate)) {
            resultMap.put("code", 0);
            resultMap.put("data", returnDate);
        } else {
            List<Map<String, Object>> dataListMap = new ArrayList<Map<String, Object>>();
            Set<String> appKeyByLesseeIdAndAppType = uarBasicAppinfoService.getAppKeyByLesseeIdAndAppType(orginId, null);

            logger.info("画像（全省份）接口查询机构id为：" + orginId);
            // 对于机构对应的用户以及公众号进行打印
            logger.info("在【" + DateUtils.getDateStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "】查询机构id为【" + orginId + "】，对应应用信息为【" + appKeyByLesseeIdAndAppType.toString() + "】");

            getGenderAndAgeAndAreaData(appKeyByLesseeIdAndAppType, dataListMap, "province");

            logger.info("机构id为【" + orginId + "】的数据打印完成【" + dataListMap + "】");

            OriginReturnRecordAllProvinceRegion originReturnRecordAllProvinceRegion = new OriginReturnRecordAllProvinceRegion();
            originReturnRecordAllProvinceRegion.setOriginId(orginId);
            originReturnRecordAllProvinceRegion.setReturnDate(currentDateStr);
            originReturnRecordAllProvinceRegion.setReturnJson(JSON.toJSONString(dataListMap));

            //接口返回记录保存mysql，缓存
            originReturnRecordAllProvinceRegionService.insert(originReturnRecordAllProvinceRegion);

            resultMap.put("code", 0);
            resultMap.put("data", JSON.toJSONString(dataListMap));
        }


        return resultMap;
    }

    /**
     * 对于appkey集合下的性别、年龄和地域的数据进行封装----生产数据
     *
     * @param appKeyByLesseeIdAndAppType appkey的集合
     * @param dataListMap                数据封装结果
     * @param regionLevel                地域聚合级别(province：省份；other：城市)
     */
    private void getGenderAndAgeAndAreaData(Set<String> appKeyByLesseeIdAndAppType, List<Map<String, Object>> dataListMap, String regionLevel) {
        if (null != appKeyByLesseeIdAndAppType && appKeyByLesseeIdAndAppType.size() > 0) {
            Map<String, Object> genderAndAgeAndRegionMap = new HashMap<String, Object>();

            Map<String, Object> genderMap = new HashMap<String, Object>();
            Map<String, Object> ageMap = new HashMap<String, Object>();
            Long maleNum = getTotalElements(appKeyByLesseeIdAndAppType, "sex", "男");
            Long female = getTotalElements(appKeyByLesseeIdAndAppType, "sex", "女");
            List<Map<String, Object>> genderTmpListMap = new ArrayList<Map<String, Object>>();
            genderMap.put("femaleNum", female);
            genderMap.put("maleNum", maleNum);
            genderTmpListMap.add(genderMap);
            genderAndAgeAndRegionMap.put("gender", genderTmpListMap);
            Long young = getTotalElements(appKeyByLesseeIdAndAppType, "age", "青年");
            Long middleNum = getTotalElements(appKeyByLesseeIdAndAppType, "age", "中年");
            Long oldNum = getTotalElements(appKeyByLesseeIdAndAppType, "age", "老年");
            ageMap.put("youngNum", young);
            ageMap.put("middleNum", middleNum);
            ageMap.put("oldNum", oldNum);
            List<Map<String, Object>> ageTmpListMap = new ArrayList<Map<String, Object>>();
            ageTmpListMap.add(ageMap);
            genderAndAgeAndRegionMap.put("age", ageTmpListMap);

            if (StringUtils.isNotBlank(regionLevel) && "province".equals(regionLevel)) {
                //地域聚合
                List<Map<String, Object>> resultProvinceRegion = getAggregationsProvince(appKeyByLesseeIdAndAppType);
                genderAndAgeAndRegionMap.put("region", resultProvinceRegion);
            } else {
                //地域聚合
                List<Map<String, Object>> resultCityRegion = getAggregationsCity(appKeyByLesseeIdAndAppType);
                genderAndAgeAndRegionMap.put("region", resultCityRegion);
            }
            dataListMap.add(genderAndAgeAndRegionMap);
        }
    }

    /**
     * 对城市进行聚合查询
     *
     * @param atSet      appkey应用标识
     * @return
     */
    public List<Map<String, Object>> getAggregationsCity(Set<String> atSet) {
        List<Map<String, Object>> resultCityRegion = new ArrayList<Map<String, Object>>();

        TransportClient client = null;
        try {
            client = ProfileESClient.getClientIns();
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.must(atQuery(atSet));
            builder.must(QueryBuilders.rangeQuery("tag_count").gt(0));

            AggregationBuilder agg = AggregationBuilders.terms("City")
                    .field("city").size(0);
            SearchResponse sr = client
                    .prepareSearch(ESConfigConstants.ES_PROFILE_INDEX_OFFLINE_USER_PROFILE)
                    .setTypes(ESConfigConstants.ES_PROFILE_TYPE_USER_TAGS)
                    .setQuery(
                            QueryBuilders.boolQuery()
                                    .filter(builder)
                    ).setSize(0).addAggregation(agg)
                    .execute().actionGet();
            Terms aggTerms = sr.getAggregations().get("City");
            List<Terms.Bucket> tag = aggTerms.getBuckets();


            for (int i = 0; i < tag.size(); i++) {
                Map<String, Object> tmpMap = new HashMap<String, Object>();
                tmpMap.put("regionName", tag.get(i).getKey().toString());
                tmpMap.put("num", tag.get(i).getDocCount());
                resultCityRegion.add(tmpMap);
            }
        } catch (IOException e) {
            logger.error("es配置信息错误");
            e.printStackTrace();
        }
        return resultCityRegion;
    }

    /**
     * 对省份进行聚合查询
     *
     * @param atSet      appkey应用标识
     * @return
     */
    public List<Map<String, Object>> getAggregationsProvince(Set<String> atSet) {
        List<Map<String, Object>> resultProvinceRegion = new ArrayList<Map<String, Object>>();

        TransportClient client = null;
        try {
            client = ProfileESClient.getClientIns();
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.must(atQuery(atSet));
            builder.must(QueryBuilders.rangeQuery("tag_count").gt(0));

            AggregationBuilder agg = AggregationBuilders.terms("Province")
                    .field("province").size(0);
            SearchResponse sr = client
                    .prepareSearch(ESConfigConstants.ES_PROFILE_INDEX_OFFLINE_USER_PROFILE)
                    .setTypes(ESConfigConstants.ES_PROFILE_TYPE_USER_TAGS)
                    .setQuery(
                            QueryBuilders.boolQuery()
                                    .filter(builder)
                    ).setSize(0).addAggregation(agg)
                    .execute().actionGet();
            Terms aggTerms = sr.getAggregations().get("Province");
            List<Terms.Bucket> tag = aggTerms.getBuckets();


            for (int i = 0; i < tag.size(); i++) {
                Map<String, Object> tmpMap = new HashMap<String, Object>();
                tmpMap.put("regionName", tag.get(i).getKey().toString());
                tmpMap.put("num", tag.get(i).getDocCount());
                resultProvinceRegion.add(tmpMap);
            }
        } catch (IOException e) {
            logger.error("es配置信息错误");
            e.printStackTrace();
        }
        return resultProvinceRegion;
    }

    /**
     * 根据指定appkey以及指定字段查询es中的总数据量
     *
     * @param atSet      appkey应用标识
     * @param queryField 待查询es标识
     * @param queryParam 查询参数值
     * @return
     */
    public Long getTotalElements(Set<String> atSet, String queryField, String queryParam) {
        TransportClient client = null;
        try {
            client = ProfileESClient.getClientIns();
        } catch (IOException e) {
            logger.error("es配置信息错误");
            e.printStackTrace();
            return 0l;
        }
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(queryField) && StringUtils.isNotBlank(queryParam)) {
            builder.must(QueryBuilders.termQuery(queryField, queryParam));
        }
        builder.must(atQuery(atSet));
        builder.must(QueryBuilders.rangeQuery("tag_count").gt(0));

        SearchResponse sr = null;
        try {
            sr = client
                    .prepareSearch(ESConfigConstants.ES_PROFILE_INDEX_OFFLINE_USER_PROFILE)
                    .setTypes(ESConfigConstants.ES_PROFILE_TYPE_USER_TAGS)
                    .setQuery(builder)
                    .setSize(0).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return sr.getHits().getTotalHits();
    }

    private QueryBuilder atQuery(Set<String> allAppKeyByMediaId) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        String queryString = StringUtils.join(allAppKeyByMediaId, ";");
        boolQuery.should(QueryBuilders.queryStringQuery("at" + ":" + queryString));
        return boolQuery;
    }
}
