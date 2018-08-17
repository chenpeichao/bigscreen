package com.hubpd.bigscreen.service.uar_profile.impl;

import com.alibaba.fastjson.JSON;
import com.hubpd.bigscreen.bean.origin_return.OriginReturnRecord;
import com.hubpd.bigscreen.bean.uar_basic.UarBasicTaskOrgin;
import com.hubpd.bigscreen.service.common.TaskGetUserAnalyseService;
import com.hubpd.bigscreen.service.origin_return.OriginReturnRecordService;
import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import com.hubpd.bigscreen.service.uar_profile.UserAnalyseService;
import com.hubpd.bigscreen.utils.Constants;
import com.hubpd.bigscreen.utils.DateUtils;
import com.hubpd.bigscreen.vo.UserAnalyseRegionVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
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

import java.util.*;

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
    //es链接实体
    private Client client;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    /**
     * 用户分析接口，计算性别，青老中，前5地域
     *
     * @param orginId 机构id
     * @return
     */
    public Map<String, Object> getUserAnalyseReturnData(String orginId) {
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

        // 根据机构id查询其下用户数据
        List<String> uarBasicUserIdListByOrginId = uarBasicUserService.findUarBasicUserIdListByOrginId(orginId);
        if (uarBasicUserIdListByOrginId == null || uarBasicUserIdListByOrginId.size() == 0) {
            logger.info("机构下没有对应的用户数据");
            resultMap.put("code", 1);
            resultMap.put("data", new ArrayList());
            return resultMap;
        }

        // 2、首先从mysql数据库中查询指定机构id的最新一条返回记录
        OriginReturnRecord originReturnRecordDB = originReturnRecordService.findOriginReturnRecordByOriginIdAndLastDate(orginId);
        if (originReturnRecordDB != null) {
            resultMap.put("code", 1);
            resultMap.put("data", StringUtils.isNotBlank(originReturnRecordDB.getReturnJson()) ? JSON.parse(originReturnRecordDB.getReturnJson()) : new ArrayList());
            return resultMap;
        } else {
            // 当mysql中没有此机构id下的数据时
            //1、向机构id大屏缓存表里面添加此机构id，
            //1.1、首先查询机构id在大屏机构表里面有没有存储
            List<UarBasicTaskOrgin> taskOriginByOriginIdInBigscreenList = uarBasicUserService.findTaskOriginByOriginIdInBigscreen(orginId);
            if (taskOriginByOriginIdInBigscreenList != null && taskOriginByOriginIdInBigscreenList.size() > 0) {
                // 当大屏机构表里存在此机构id对应信息，启动新线程执行接口调用
                taskGetUserAnalyseService.getUserAnalyse(orginId);
                resultMap.put("code", 0);
                resultMap.put("message", "由于此机构之前数据未抓取，请1小时后再试！！");
                return resultMap;
            } else {
                // 当大屏机构表里不存在此机构id对应信息，先保存此机构id信息到大屏机构表，在启动新线程执行接口调用
                taskGetUserAnalyseService.getUserAnalyse(orginId);
                UarBasicTaskOrgin uarBasicTaskOrgin = new UarBasicTaskOrgin();
                uarBasicTaskOrgin.setOrgId(orginId);
                uarBasicTaskOrgin.setStatus(1);
                uarBasicUserService.saveTaskOrgin(uarBasicTaskOrgin);
                resultMap.put("code", 0);
                resultMap.put("message", "由于之前未添加此机构，请1小时后再试！！");
                return resultMap;
            }
            //2、子线程调用任务，缓存数据
        }
    }

    private Map<String, Long> getSexStatistic(String at, String aggreParam) {
        Map<String, Long> resultMap1 = new HashMap<String, Long>();
        Map<String, Long> resultMap2 = new HashMap<String, Long>();
        try {
            //1.创建查询条件，也就是QueryBuild
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.must(QueryBuilders.queryStringQuery("at : " + at));
            builder.must(QueryBuilders.rangeQuery("tag_count").gt(0));
            //2.构建查询
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            //2.0 设置QueryBuilder
            nativeSearchQueryBuilder.withQuery(builder);
            //2.1设置搜索类型，默认值就是QUERY_THEN_FETCH，参考https://blog.csdn.net/wulex/article/details/71081042
            nativeSearchQueryBuilder.withSearchType(SearchType.QUERY_THEN_FETCH);//指定索引的类型，只先从各分片中查询匹配的文档，再重新排序和排名，取前size个文档
            //2.2指定索引库和文档类型
            //nativeSearchQueryBuilder.withIndices("myBlog").withTypes("blog");//指定要查询的索引库的名称和类型，其实就是我们文档@Document中设置的indedName和type
            //2.3重点来了！！！指定聚合函数,本例中以某个字段分组聚合为例（可根据你自己的聚合查询需求设置）
            //该聚合函数解释：计算该字段(假设为username)在所有文档中的出现频次，并按照降序排名（常用于某个字段的热度排名）
            TermsBuilder termsAggregation = AggregationBuilders.terms("term").field(aggreParam).order(Terms.Order.count(false)).size(2);
            nativeSearchQueryBuilder.addAggregation(termsAggregation);
            //2.4构建查询对象
            NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();

            //3.3方法3,通过elasticSearch模板elasticsearchTemplate.query()方法查询,获得聚合(常用)
            Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQuery, new ResultsExtractor<Aggregations>() {
                @Override
                public Aggregations extract(SearchResponse response) {
                    return response.getAggregations();
                }
            });
            //转换成map集合
            Map<String, Aggregation> aggregationMap = aggregations.asMap();
            //获得对应的聚合函数的聚合子类，该聚合子类也是个map集合,里面的value就是桶Bucket，我们要获得Bucket
            StringTerms stringTerms = (StringTerms) aggregationMap.get("term");
            //获得所有的桶
            List<Terms.Bucket> tag = stringTerms.getBuckets();

            for (int i = 0; i < tag.size(); i++) {
                resultMap1.put(tag.get(i).getKey().toString(), tag.get(i).getDocCount());
            }
            resultMap2.put("maleNum", resultMap1.get("男") == null || resultMap1.get("男") == 0l ? 0l : resultMap1.get("男"));
            resultMap2.put("female", resultMap1.get("女") == null || resultMap1.get("女") == 0l ? 0l : resultMap1.get("女"));
            logger.info("【" + at + "】性别数据为：" + resultMap1);
        } catch (Exception e) {
            logger.error("【" + at + "】查询性别数据失败", e);
        } finally {
            return resultMap2;
        }
    }

    // 年龄分布
    private Map<String, Long> getAgeStatistic(String at, String aggreParam) {
        Map<String, Long> resultMap1 = new HashMap<String, Long>();
        Map<String, Long> resultMap2 = new HashMap<String, Long>();
        try {
            //1.创建查询条件，也就是QueryBuild
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.must(QueryBuilders.queryStringQuery("at : " + at));
            builder.must(QueryBuilders.rangeQuery("tag_count").gt(0));
            //2.构建查询
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            //2.0 设置QueryBuilder
            nativeSearchQueryBuilder.withQuery(builder);
            //2.1设置搜索类型，默认值就是QUERY_THEN_FETCH，参考https://blog.csdn.net/wulex/article/details/71081042
            nativeSearchQueryBuilder.withSearchType(SearchType.QUERY_THEN_FETCH);//指定索引的类型，只先从各分片中查询匹配的文档，再重新排序和排名，取前size个文档
            //2.2指定索引库和文档类型
            //nativeSearchQueryBuilder.withIndices("myBlog").withTypes("blog");//指定要查询的索引库的名称和类型，其实就是我们文档@Document中设置的indedName和type
            //2.3重点来了！！！指定聚合函数,本例中以某个字段分组聚合为例（可根据你自己的聚合查询需求设置）
            //该聚合函数解释：计算该字段(假设为username)在所有文档中的出现频次，并按照降序排名（常用于某个字段的热度排名）
            TermsBuilder termsAggregation = AggregationBuilders.terms("term").field(aggreParam).order(Terms.Order.count(false)).size(3);
            nativeSearchQueryBuilder.addAggregation(termsAggregation);
            //2.4构建查询对象
            NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();

            //3.3方法3,通过elasticSearch模板elasticsearchTemplate.query()方法查询,获得聚合(常用)
            Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQuery, new ResultsExtractor<Aggregations>() {
                @Override
                public Aggregations extract(SearchResponse response) {
                    return response.getAggregations();
                }
            });
            //转换成map集合
            Map<String, Aggregation> aggregationMap = aggregations.asMap();
            //获得对应的聚合函数的聚合子类，该聚合子类也是个map集合,里面的value就是桶Bucket，我们要获得Bucket
            StringTerms stringTerms = (StringTerms) aggregationMap.get("term");
            //获得所有的桶
            List<Terms.Bucket> tag = stringTerms.getBuckets();

            for (int i = 0; i < tag.size(); i++) {
                resultMap1.put(tag.get(i).getKey().toString(), tag.get(i).getDocCount());
            }
            resultMap2.put("young", resultMap1.get("青年") == null || resultMap1.get("青年") == 0l ? 0l : resultMap1.get("青年"));
            resultMap2.put("middleNum", resultMap1.get("中年") == null || resultMap1.get("中年") == 0l ? 0l : resultMap1.get("中年"));
            resultMap2.put("oldNum", resultMap1.get("老年") == null || resultMap1.get("老年") == 0l ? 0l : resultMap1.get("老年"));
            logger.info("【" + at + "】年龄数据为：" + resultMap1);
        } catch (Exception e) {
            logger.error("【" + at + "】查询年龄数据失败", e);
        } finally {
            return resultMap2;
        }
    }

    // 中国地图--topN
    private Map<String, List<UserAnalyseRegionVO>> getProvinceStatistic(String at, String aggreParam, int topN) {
        Map<String, List<UserAnalyseRegionVO>> resultMap = new HashMap<String, List<UserAnalyseRegionVO>>();
        Map<String, Object> resultMap1 = new HashMap<String, Object>();
        try {
            //1.创建查询条件，也就是QueryBuild
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.must(QueryBuilders.queryStringQuery("at : " + at));
            builder.must(QueryBuilders.rangeQuery("tag_count").gt(0));
            //2.构建查询
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            //2.0 设置QueryBuilder
            nativeSearchQueryBuilder.withQuery(builder);
            //2.1设置搜索类型，默认值就是QUERY_THEN_FETCH，参考https://blog.csdn.net/wulex/article/details/71081042
            nativeSearchQueryBuilder.withSearchType(SearchType.QUERY_THEN_FETCH);//指定索引的类型，只先从各分片中查询匹配的文档，再重新排序和排名，取前size个文档
            //2.2指定索引库和文档类型
            //nativeSearchQueryBuilder.withIndices("myBlog").withTypes("blog");//指定要查询的索引库的名称和类型，其实就是我们文档@Document中设置的indedName和type
            //2.3重点来了！！！指定聚合函数,本例中以某个字段分组聚合为例（可根据你自己的聚合查询需求设置）
            //该聚合函数解释：计算该字段(假设为username)在所有文档中的出现频次，并按照降序排名（常用于某个字段的热度排名）
            TermsBuilder termsAggregation = AggregationBuilders.terms("term").field(aggreParam).order(Terms.Order.count(false)).size(topN);
            nativeSearchQueryBuilder.addAggregation(termsAggregation);
            //2.4构建查询对象
            NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();

            //3.3方法3,通过elasticSearch模板elasticsearchTemplate.query()方法查询,获得聚合(常用)
            Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQuery, new ResultsExtractor<Aggregations>() {
                @Override
                public Aggregations extract(SearchResponse response) {
                    return response.getAggregations();
                }
            });
            //转换成map集合
            Map<String, Aggregation> aggregationMap = aggregations.asMap();
            //获得对应的聚合函数的聚合子类，该聚合子类也是个map集合,里面的value就是桶Bucket，我们要获得Bucket
            StringTerms stringTerms = (StringTerms) aggregationMap.get("term");
            //获得所有的桶
            List<Terms.Bucket> tag = stringTerms.getBuckets();


            for (int i = 0; i < tag.size(); i++) {
                resultMap1.put(tag.get(i).getKey().toString(), tag.get(i).getDocCount());
            }

            List<UserAnalyseRegionVO> userAnalyseRegionVOList = new ArrayList<UserAnalyseRegionVO>();
            for (int i = 0; i < tag.size(); i++) {
                UserAnalyseRegionVO userAnalyseRegionVO = new UserAnalyseRegionVO();
                userAnalyseRegionVO.setRegionName(tag.get(i).getKey().toString());
                userAnalyseRegionVO.setNum(tag.get(i).getDocCount());

                userAnalyseRegionVOList.add(userAnalyseRegionVO);
            }
            resultMap.put("region", userAnalyseRegionVOList);
            logger.info("【" + at + "】top" + topN + "地域数据为：" + resultMap);
        } catch (Exception e) {
            logger.error("【" + at + "】查询top" + topN + "地域数据失败", e);
        } finally {
            return resultMap;
        }
    }

    // 性别分布
    private Map<String, Long> getSexStatistic(String at) {
        Map<String, Long> resultMap1 = new HashMap<String, Long>();
        Map<String, Long> resultMap2 = new HashMap<String, Long>();
        try {
            AggregationBuilder agg = AggregationBuilders.terms("term")
                    .field("sex").size(0);
            SearchResponse response = client.prepareSearch(Constants.PROFILE_ES_INDEX).setTypes(Constants.PROFILE_ES_TYPE)
                    .setQuery(QueryBuilders.boolQuery().filter(atQuery(at))).setSize(0).addAggregation(agg)
                    .execute().actionGet();

            Terms aggTerms = response.getAggregations().get("term");
            List<Terms.Bucket> tag = aggTerms.getBuckets();
            for (int i = 0; i < tag.size(); i++) {
                String sex = tag.get(i).getKey().toString();
                if(sex.equals("男")) {
                    sex = "男";
                } else {
                    sex = "女";
                }
                resultMap1.put(sex, tag.get(i).getDocCount());
            }
            resultMap2.put("maleNum", resultMap1.get("男") == null || resultMap1.get("男") == 0l ? 0l : resultMap1.get("男"));
            resultMap2.put("female", resultMap1.get("女") == null || resultMap1.get("女") == 0l ? 0l : resultMap1.get("女"));
            logger.info("【" + at + "】性别数据为：" + resultMap1);
        } catch (Exception e) {
            logger.error("【" + at + "】查询性别数据失败", e);
        } finally {
            return resultMap2;
        }
    }


    // 年龄分布
    private Map<String, Long> getAgeStatistic(String at) {
        Map<String, Long> resultMap1 = new HashMap<String, Long>();
        Map<String, Long> resultMap2 = new HashMap<String, Long>();
        try {
            AggregationBuilder agg = AggregationBuilders.terms("term")
                    .field("age").size(0);
            SearchResponse response = client.prepareSearch(Constants.PROFILE_ES_INDEX).setTypes(Constants.PROFILE_ES_TYPE)
                    .setQuery(QueryBuilders.boolQuery().filter(atQuery(at))).setSize(0).addAggregation(agg)
                    .execute().actionGet();
            Terms aggTerms = response.getAggregations().get("term");
            List<Terms.Bucket> tag = aggTerms.getBuckets();

            for (int i = 0; i < tag.size(); i++) {
                resultMap1.put(tag.get(i).getKey().toString(), tag.get(i).getDocCount());
            }
            resultMap2.put("young", resultMap1.get("青年") == null || resultMap1.get("青年") == 0l ? 0l : resultMap1.get("青年"));
            resultMap2.put("middleNum", resultMap1.get("中年") == null || resultMap1.get("中年") == 0l ? 0l : resultMap1.get("中年"));
            resultMap2.put("oldNum", resultMap1.get("老年") == null || resultMap1.get("老年") == 0l ? 0l : resultMap1.get("老年"));
            logger.info("【" + at + "】年龄数据为：" + resultMap1);
        } catch (Exception e) {
            logger.error("【" + at + "】查询年龄数据失败", e);
        } finally {
            return resultMap2;
        }
    }

    // 中国地图--topN
    private Map<String, List<UserAnalyseRegionVO>> getProvinceStatistic(String at, int topN) {
        Map<String, List<UserAnalyseRegionVO>> resultMap = new HashMap<String, List<UserAnalyseRegionVO>>();
        try {
            AggregationBuilder agg = AggregationBuilders.terms("term")
                    .field("province").size(topN);
            SearchResponse response = client.prepareSearch(Constants.PROFILE_ES_INDEX).setTypes(Constants.PROFILE_ES_TYPE)
                    .setQuery(QueryBuilders.boolQuery().filter(atQuery(at))).setSize(topN).addAggregation(agg)
                    .execute().actionGet();
            Terms aggTerms = response.getAggregations().get("term");
            List<Terms.Bucket> tag = aggTerms.getBuckets();

            List<UserAnalyseRegionVO> userAnalyseRegionVOList = new ArrayList<UserAnalyseRegionVO>();
            for (int i = 0; i < tag.size(); i++) {
                UserAnalyseRegionVO userAnalyseRegionVO = new UserAnalyseRegionVO();
                userAnalyseRegionVO.setRegionName(tag.get(i).getKey().toString());
                userAnalyseRegionVO.setNum(tag.get(i).getDocCount());

                userAnalyseRegionVOList.add(userAnalyseRegionVO);
            }
            resultMap.put("region", userAnalyseRegionVOList);
            logger.info("【" + at + "】top" + topN + "地域数据为：" + resultMap);
        } catch (Exception e) {
            logger.error("【" + at + "】查询top" + topN + "地域数据失败", e);
        } finally {
            return resultMap;
        }
    }



    private QueryBuilder atQuery(String at) {
        return mustQuery(queryStringQuery("at", at), queryHasCount());
    }

    // 同时符合条件
    private QueryBuilder mustQuery(QueryBuilder... querys) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (QueryBuilder query : querys) {
            boolQuery.must(query);
        }
        return boolQuery;
    }

    private QueryBuilder queryStringQuery(String field, String value) {
        return QueryBuilders.queryStringQuery(field + ":" + value );
    }

    // 查询标签数大于0
    private QueryBuilder queryHasCount() {
        return QueryBuilders.boolQuery()
                .filter(QueryBuilders.rangeQuery("tag_count").gt(0));
    }


    /**
     * 用户分析接口，计算性别，青老中，前5地域
     *
     * @param orginId 机构id
     * @return
     */
    public Map<String, Object> getUserAnalyse_bak(String orginId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String currentDateStr = DateUtils.getDateStrByDate(new Date(), "yyyy-MM-dd");

        // 根据机构id查询其下用户数据
        List<String> uarBasicUserIdListByOrginId = uarBasicUserService.findUarBasicUserIdListByOrginId(orginId);
        if (uarBasicUserIdListByOrginId == null || uarBasicUserIdListByOrginId.size() == 0) {
            logger.info("机构下没有对应的用户数据");
            resultMap.put("code", 1);
            resultMap.put("data", new ArrayList());
            return resultMap;
        }

        // 首先从mysql数据库中查询数据，如果有数据，则直接返回，没有则进行es计算返回，并保存查询天和机构的数据到mysql数据库缓存
        String originReturnRecordStr = originReturnRecordService.findOriginReturnRecordByOriginId(orginId, currentDateStr);
        if (StringUtils.isNotBlank(originReturnRecordStr)) {
            resultMap.put("code", 1);
            resultMap.put("data", JSON.parse(originReturnRecordStr));
            return resultMap;
        }

        // 根据用户id列表，查询用户下对应的所有网站和移动应用的appaccount(即应用标识at)（返回结果为map<应用中文名，(应用appaccount，当为移动应用时为，Android和ios的appkey)>）
        Map<String, List<String>> appaccountMap = uarBasicUserService.findAppaccountListByUserBasicUserIdList(uarBasicUserIdListByOrginId);

        // 对于机构对应的用户以及公众号进行打印
        logger.info("在【" + DateUtils.getDateStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "】查询机构id为【" + orginId + "】" + "对应用户id【" + uarBasicUserIdListByOrginId.toString() + "】，对应应用信息为【" + appaccountMap.toString() + "】");

//        List<UserAnalyseVO> userAnalyseVOList = new ArrayList<UserAnalyseVO>();
//        UserAnalyseVO userAnalyseVO = new UserAnalyseVO();
//        //根据应用名称，获取应用的appacount(即应用的at)
//        Map<String, Object> tmpMap = new HashMap<String, Object>();
//        String appcount = "UAR-000139_459";
//        Long maleNum = getTotalElements(appcount, "sex", "男");
//        Long female = getTotalElements(appcount, "sex", "女");
//        userAnalyseVO.getGender().put("female", female);
//        userAnalyseVO.getGender().put("maleNum", maleNum);
//        Long young = getTotalElements(appcount, "age", "青年");
//        Long middleNum = getTotalElements(appcount, "age", "中年");
//        Long oldNum = getTotalElements(appcount, "age", "老年");
//        userAnalyseVO.getAge().put("young", young);
//        userAnalyseVO.getAge().put("middleNum", middleNum);
//        userAnalyseVO.getAge().put("oldNum", oldNum);
//
//        List<String> allDicRegionName = dicRegionService.findAllDicRegionName();
//        List<UserAnalyseRegionVO> userAnalyseRegionVOList = new ArrayList<UserAnalyseRegionVO>();
//        for (String regionName : allDicRegionName) {
//            UserAnalyseRegionVO userAnalyseRegionVO = new UserAnalyseRegionVO();
//            userAnalyseRegionVO.setRegionName(regionName);
//            userAnalyseRegionVO.setNum(getTotalElements(appcount, "province", regionName));
//            userAnalyseRegionVOList.add(userAnalyseRegionVO);
//        }
//        userAnalyseVO.setRegion(userAnalyseRegionVOList);
//
//        userAnalyseVOList.add(userAnalyseVO);
//        System.out.println(userAnalyseVO);

//        Map<String, Long> sexStatistic = getSexStatistic(appcount, "sex");
//        Map<String, Long> ageStatistic = getAgeStatistic(appcount, "age");
//        Map<String, List<UserAnalyseRegionVO>> provinceStatistic = getProvinceStatistic(appcount, "province", 5);
//        tmpMap.put("sex_" + appcount, sexStatistic);
//        tmpMap.put("age_" + appcount, ageStatistic);
//        tmpMap.put("province_" + appcount, provinceStatistic);

        List<Object> dataList = new ArrayList<Object>();
        int count = 0;
        for (String appName : appaccountMap.keySet()) {
            //根据应用名称，获取应用的appacount(即应用的at)
            List<String> appaccountList = appaccountMap.get(appName);

            Map<String, Object> tmpMap = new HashMap<String, Object>();
            if (appaccountList != null && appaccountList.size() > 0) {
                Map<String, Long> sexStatistic = getSexStatistic(appaccountList.get(0));
                Map<String, Long> ageStatistic = getAgeStatistic(appaccountList.get(0));
                Map<String, List<UserAnalyseRegionVO>> provinceStatistic = getProvinceStatistic(appaccountList.get(0), 5);
                tmpMap.put("gender", sexStatistic);
                tmpMap.put("age", ageStatistic);
                tmpMap.put("region", provinceStatistic.get("region"));
                if (appaccountList.size() > 1) {
                    Map<String, Long> sexStatistic2 = getSexStatistic(appaccountList.get(1));
                    Map<String, Long> ageStatistic2 = getAgeStatistic(appaccountList.get(1));
                    Map<String, List<UserAnalyseRegionVO>> provinceStatistic2 = getProvinceStatistic(appaccountList.get(1), 5);
                    sexStatistic.put("female", sexStatistic.get("female") + sexStatistic2.get("female"));
                    sexStatistic.put("maleNum", sexStatistic.get("maleNum") + sexStatistic2.get("maleNum"));
                    ageStatistic.put("young", ageStatistic.get("young") + ageStatistic2.get("young"));
                    ageStatistic.put("middleNum", ageStatistic.get("middleNum") + ageStatistic2.get("middleNum"));
                    ageStatistic.put("oldNum", ageStatistic.get("oldNum") + ageStatistic2.get("oldNum"));

                    List<UserAnalyseRegionVO> userAnalyseRegionVOList1 = (List<UserAnalyseRegionVO>) provinceStatistic.get("region");
                    List<UserAnalyseRegionVO> userAnalyseRegionVOList2 = (List<UserAnalyseRegionVO>) provinceStatistic2.get("region");
                    userAnalyseRegionVOList1.addAll(userAnalyseRegionVOList2);
                    userAnalyseRegionVOList1.sort(new Comparator<UserAnalyseRegionVO>() {
                        @Override
                        public int compare(UserAnalyseRegionVO o1, UserAnalyseRegionVO o2) {
                            Long tmp = o2.getNum() - o1.getNum();
                            return tmp.intValue();
                        }
                    });

                    tmpMap.put("region", userAnalyseRegionVOList1.subList(0, userAnalyseRegionVOList1.size() > 5 ? 5 : userAnalyseRegionVOList1.size()));
                }
            }
            tmpMap.put("appName", appName);
            dataList.add(tmpMap);
        }

        logger.info("机构id为【" + orginId + "】的数据打印完成");
        // 对于指定机构在指定查询日期的返回数据，进行mysql数据库的缓存
        String returnDate = originReturnRecordService.findOriginReturnRecordByOriginId(orginId, currentDateStr);
        if (StringUtils.isBlank(returnDate)) {
            OriginReturnRecord originReturnRecord = new OriginReturnRecord();
            originReturnRecord.setOriginId(orginId);
            originReturnRecord.setReturnDate(currentDateStr);
            originReturnRecord.setReturnJson(JSON.toJSONString(dataList));

            //接口返回记录保存mysql，缓存
            originReturnRecordService.insert(originReturnRecord);
        }

        resultMap.put("code", 1);
        resultMap.put("data", dataList);
//        resultMap.put("data", userAnalyseVOList);
        return resultMap;
    }


    //    /**
//     * 用户分析接口，计算性别，青老中，前5地域
//     * @param orginId           机构id
//     * @return
//     */
//    public Map<String, Object> getUserAnalyse(String orginId) {
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//
//        //根据机构id查询其下用户数据
//        List<String> uarBasicUserIdListByOrginId = uarBasicUserService.findUarBasicUserIdListByOrginId(orginId);
//        if(uarBasicUserIdListByOrginId == null || uarBasicUserIdListByOrginId.size() == 0) {
//            logger.info("机构下没有对应的用户数据");
//            resultMap.put("code", 1);
//            resultMap.put("data", new ArrayList());
//            return resultMap;
//        }
//
//        //根据用户id列表，查询用户下对应的所有网站和移动应用的appaccount(即应用标识at)（返回结果为map<应用中文名，(应用appaccount，当为移动应用时为，Android和ios的appkey)>）
//        Map<String, List<String>> appaccountMap = uarBasicUserService.findAppaccountListByUserBasicUserIdList(uarBasicUserIdListByOrginId);
//
////        List<UserAnalyse> dataList = userAnalyseMapper.findByAtAndTag_CountAndAge("UAR-000159_219", 4, "青年");
//
//        UserAnalyse mx0con2itd20qodiol53jow7g4npfhb9 = userAnalyseMapper.findOne("mx0con2itd20qodiol53jow7g4npfhb9");
//
//        Pageable pageable = new PageRequest(0,3);
//        List dev = userAnalyseMapper.findByAtAndTagCountAndAge("青年", "UAR-000159_219", pageable);
//
////        Long abc = userAnalyseMapper.countByAtLikeAndAgeAndTag_CountGreaterThan("UAR-000159_219", "青年", 0);
//
////        System.out.println(dataList);
//
//        resultMap.put("code", 1);
//        resultMap.put("data", 1);
//        return resultMap;
//    }
}
