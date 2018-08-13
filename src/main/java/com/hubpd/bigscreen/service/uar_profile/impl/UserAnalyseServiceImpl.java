package com.hubpd.bigscreen.service.uar_profile.impl;

import com.hubpd.bigscreen.bean.uar_profile.Article;
import com.hubpd.bigscreen.mapper.uar_profile.UserAnalyseMapper;
import com.hubpd.bigscreen.service.uar_profile.UserAnalyseService;
import com.hubpd.bigscreen.service.weishu_pdmi.impl.WeiShuPdmiUserServiceImpl;
import com.hubpd.bigscreen.utils.Constants;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * 用户分析--性别，年龄，地域
 * @author cpc
 * @create 2018-08-11 20:07
 **/
@Service
@Transactional
public class UserAnalyseServiceImpl implements UserAnalyseService {
    private Logger logger = Logger.getLogger(UserAnalyseServiceImpl.class);

    @Autowired
    private UserAnalyseMapper userAnalyseMapper;

    @Autowired
    private Client client;

    public void  test() {
        JSONObject json = new JSONObject();
        try {
            AggregationBuilder agg = AggregationBuilders.terms("term")
                    .field("sex").size(0);
            SearchResponse response = client.prepareSearch(Constants.PROFILE_ES_INDEX).setTypes(Constants.PROFILE_ES_TYPE)
                    .setQuery(QueryBuilders.boolQuery().filter(atQuery("UAR-000139_459"))).setSize(0).addAggregation(agg)
                    .execute().actionGet();
            Terms aggTerms = response.getAggregations().get("term");
            List<Terms.Bucket> tag = aggTerms.getBuckets();
            for (int i = 0; i < tag.size(); i++) {
                String sex = tag.get(i).getKey().toString();
                if(sex.equals("男")) {
                    //44034924
                    sex = "male";
                } else {
                    sex = "female";
                }
                json.put(sex, tag.get(i).getDocCount());
            }
            logger.info("getDev_statistic-----" + json);

            getAge_statistic("UAR-000139_459");
            getProvince_statistic("UAR-000139_459", 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 年龄分布
    private JSONObject getAge_statistic(String at) throws Exception {
        String index = Constants.PROFILE_ES_INDEX;
        String type = Constants.PROFILE_ES_TYPE;

        AggregationBuilder agg = AggregationBuilders.terms("term")
                .field("age").size(0);
        SearchResponse response = client.prepareSearch(index).setTypes(type)
                .setQuery(QueryBuilders.boolQuery().filter(atQuery(at))).setSize(0).addAggregation(agg)
                .execute().actionGet();
        Terms aggTerms = response.getAggregations().get("term");
        List<Terms.Bucket> tag = aggTerms.getBuckets();
        JSONObject json = new JSONObject();
        for (int i = 0; i < tag.size(); i++) {
            json.put(tag.get(i).getKey().toString(), tag.get(i).getDocCount());
        }
        logger.info("getDev_statistic-----" + json);
        return json;
    }

    // 中国地图
    public JSONObject getProvince_statistic(String at, int topN) throws Exception {
        String index = Constants.PROFILE_ES_INDEX;
        String type = Constants.PROFILE_ES_TYPE;

        AggregationBuilder agg = AggregationBuilders.terms("term")
                .field("province").size(topN);
        SearchResponse response = client.prepareSearch(index).setTypes(type)
                .setQuery(QueryBuilders.boolQuery().filter(atQuery(at))).setSize(topN).addAggregation(agg)
                .execute().actionGet();
        Terms aggTerms = response.getAggregations().get("term");
        List<Terms.Bucket> tag = aggTerms.getBuckets();
        JSONObject json = new JSONObject();
        for (int i = 0; i < tag.size(); i++) {
            json.put(tag.get(i).getKey().toString(), tag.get(i).getDocCount());
        }
        logger.info("getDev_statistic-----" + json);
        return json;
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


//    public void  testSpringBootMapp() {
//        String queryString = "人民网";
//        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(queryString);
//        Iterable<Article> searchResult = userAnalyseMapper.search(builder);
//
//        QueryBuilder queryBuilder = QueryBuilders.termQuery("itemId", "51e91d2eb799ac57f95970edc84c5990");
//        Iterable<Article> searchResult = userAnalyseMapper.search(queryBuilder);
//
//        Iterable<Article> search = userAnalyseMapper.search(queryBuilder);
//
//        Iterator<Article> iterator = searchResult.iterator();
//        while(iterator.hasNext()) {
//            Article next = iterator.next();
//            System.out.println(next);
//        }
//    }
}
