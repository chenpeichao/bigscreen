package com.hubpd.bigscreen.service.statistic_analyse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hubpd.bigscreen.bean.uar_basic.UarBasicAppinfo;
import com.hubpd.bigscreen.bean.uar_statistic.UarStatisticWebAtDay;
import com.hubpd.bigscreen.dto.CrtOriginAndTraceCountDTO;
import com.hubpd.bigscreen.dto.UarAppkeyAndCrtMediaIdDTO;
import com.hubpd.bigscreen.mapper.uar_statistic.UarStatisticWebAtDayMapper;
import com.hubpd.bigscreen.service.statistic_analyse.AppActivityUserAtDayService;
import com.hubpd.bigscreen.service.statistic_analyse.StatisticAnalyseService;
import com.hubpd.bigscreen.service.statistic_analyse.WebAtCLNDayService;
import com.hubpd.bigscreen.service.uar_basic.MediaService;
import com.hubpd.bigscreen.service.uar_basic.UarBasicAppinfoService;
import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import com.hubpd.bigscreen.utils.*;
import com.hubpd.bigscreen.vo.StatisticAnalyseTmpVO;
import com.hubpd.bigscreen.vo.StatisticAnalyseVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 运营分析相关
 *
 * @author cpc
 * @create 2018-09-13 15:50
 **/
@Service
public class StatisticAnalyseServiceImpl implements StatisticAnalyseService {
    private Logger logger = Logger.getLogger(StatisticAnalyseServiceImpl.class);

    @Autowired
    private UarBasicUserService uarBasicUserService;
    @Autowired
    private UarBasicAppinfoService uarBasicAppinfoService;
    @Autowired
    private MediaService mediaService;

    @Autowired
    private UarStatisticWebAtDayMapper uarStatisticWebAtDayMapper;
    @Autowired
    private WebAtCLNDayService webAtCLNDayService;
    @Autowired
    private AppActivityUserAtDayService appActivityUserAtDayService;

    /**
     * 根据机构id和查询时间查询pv、uv以及crt的相关原创数和转载数---默认查询昨天
     *
     * @param orginIdStr
     * @param searchDate
     * @return
     */
    public Map<String, Object> getStatisticAnalyse(String orginIdStr, Date searchDate) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String searchDateStr = DateUtils.getDateStrByDate(searchDate, "yyyyMMdd");

        Date currentDate = new Date();

        //1、首先查询传递的机构id，是否在uar中是有效的机构id
        List<String> allOriginIdListInUar = uarBasicUserService.findAllOriginIdListInUar();
        if (!allOriginIdListInUar.contains(orginIdStr)) {
            //传递的机构id在uar中不存在时，返回null数据
            resultMap.put("code", 0);
            resultMap.put("message", "系统中不存在此机构id");
            return resultMap;
        }

        // 1、根据用户id列表，查询用户下对应的所有网站和移动应用的appaccount(即应用标识at)（返回结果为map<应用中文名，(应用appaccount，当为移动应用时为，Android和ios的appkey)>）
        Map<String, List<String>> appaccountMap = uarBasicAppinfoService.findAppaccountListByOrgId(orginIdStr);
        // 对于机构对应的用户以及公众号进行打印
        logger.info("在【" + DateUtils.getDateStrByDate(currentDate, "yyyy-MM-dd HH:mm:ss") + "】查询机构id为【" + orginIdStr + "】，对应应用信息为【" + appaccountMap.toString() + "】");

        List<StatisticAnalyseTmpVO> statisticAnalyseTmpVOList = new ArrayList<StatisticAnalyseTmpVO>();

        //2、通过应用appkey获取pv和uv数据
        for (String appName : appaccountMap.keySet()) {
            StatisticAnalyseTmpVO statisticAnalyseTmpVO = new StatisticAnalyseTmpVO();

            //根据应用名称，获取应用的appacount(即应用的at)
            List<String> appaccountList = appaccountMap.get(appName);
            statisticAnalyseTmpVO.setAppName(appName);
            for (String appaccount : appaccountList) {
                statisticAnalyseTmpVO.setUarAppkey(StringUtils.isNotBlank(statisticAnalyseTmpVO.getUarAppkey()) ? statisticAnalyseTmpVO.getUarAppkey() + "|" + appaccount : appaccount);

                //首先判断该appaccount是否为移动应用的，如果是，则调用app_at_day获取，否则则通过web_at_day获取数据
                UarBasicAppinfo uarBasicAppinfo = uarBasicAppinfoService.findAppInfoByAppAccountOrAppAccount2(appaccount);
                UarStatisticWebAtDay uarStatisticWebAtDay = new UarStatisticWebAtDay();
                //由于移动应用和网站的数据来自不同的表，所以需要判断
                if (uarBasicAppinfo.getApptype() != null && uarBasicAppinfo.getApptype() == Constants.UAR_APP_TYPE_APP) {
                    //此为移动应用
                    uarStatisticWebAtDay = this.selectPVAndUVByAtAndDateApp(appaccount, searchDateStr);
                } else {
                    //此为网站
                    uarStatisticWebAtDay = this.selectPVAndUVByAtAndDateWeb(appaccount, searchDateStr);
                }
                if (uarStatisticWebAtDay != null) {
                    statisticAnalyseTmpVO.setPv(statisticAnalyseTmpVO.getPv() + uarStatisticWebAtDay.getPv());
                    statisticAnalyseTmpVO.setUv(statisticAnalyseTmpVO.getUv() + uarStatisticWebAtDay.getUv());
                }
            }

            statisticAnalyseTmpVOList.add(statisticAnalyseTmpVO);
        }

        //-----------------crt信息获取--------------------------
        //1、根据uar的机构id获取crt机构id的url请求封装
        String crtAdminId = "";         //crt机构id
        String getCrtAdminIdByUarOrgIdUrl = Constants.PDMI_INTERFACE_HOST + Constants.PDMI_INTERFACE_URL_GET_ADMIN_ID_BY_UAR_ORG_ID + "?" + "uar=" + orginIdStr;
        logger.info("调用【获取uar和crt机构id对应关系】接口：" + getCrtAdminIdByUarOrgIdUrl);
        String returnJsonGetCrtAdminIdByUarOrgIdUrl = HttpUtils.doGet(getCrtAdminIdByUarOrgIdUrl);
        if (StringUtils.isNotBlank(returnJsonGetCrtAdminIdByUarOrgIdUrl)) {
            Map<String, String> parseGetCrtAdminIdByUarOrgIdUrl = (Map<String, String>) JSON.parse(returnJsonGetCrtAdminIdByUarOrgIdUrl);
            //获取到crt的机构id
            crtAdminId = parseGetCrtAdminIdByUarOrgIdUrl.get("crt");
        } else {
            logger.info("用户调用的机构id【" + orginIdStr + "】未获取到crt端的机构id---first");
        }

        //2、根据crt机构id，获取uar的appkey和crt的mediaId的对应关系数据
        List<UarAppkeyAndCrtMediaIdDTO> uarAppkeyAndCrtMediaIdDTOList = new ArrayList<UarAppkeyAndCrtMediaIdDTO>();
        String getAppKeyAndMediaIdRelationUrl = Constants.PDMI_INTERFACE_HOST + Constants.PDMI_INTERFACE_URL_CRT_MEDIA_RELATION_BY_UAR_ORG_ID + "?" + "uar=" + orginIdStr;
        logger.info("调用【获取uar的appkey和crt的mediaId对应关系】接口：" + getAppKeyAndMediaIdRelationUrl);
        String returnJsonGetAppKeyAndMediaIdRelationUrl = HttpUtils.doGet(getAppKeyAndMediaIdRelationUrl);
        if (StringUtils.isNotBlank(returnJsonGetAppKeyAndMediaIdRelationUrl)) {
            uarAppkeyAndCrtMediaIdDTOList = JSON.parseArray(returnJsonGetAppKeyAndMediaIdRelationUrl, UarAppkeyAndCrtMediaIdDTO.class);
        } else {
            logger.info("用户调用的机构id【" + orginIdStr + "】未获取到crt端的mediaId与uar端的appkey对应关系---second");
        }

        //3、根据crt机构id，查询网站和app的相关原创和转载数据
        List<CrtOriginAndTraceCountDTO> crtOriginAndTraceCountDTOResultList = new ArrayList<CrtOriginAndTraceCountDTO>();
        if (StringUtils.isNotBlank(crtAdminId) && uarAppkeyAndCrtMediaIdDTOList.size() > 0) {
            Map<String, String> jsonObject = new HashMap<String, String>();
            Long timesMillis = System.currentTimeMillis();
            jsonObject.put("clientCode", Constants.CLIENT_CODE);
            jsonObject.put("secretCode", Md5Utils.getMD5OfStr(Constants.CLIENT_CODE + Constants.SECRET_KEY + timesMillis));
            jsonObject.put("timesMillis", "" + timesMillis);
            jsonObject.put("adminId", crtAdminId);
            jsonObject.put("date", DateUtils.getDateStrByDate(searchDate, "yyyy-MM-dd"));
            if (StringUtils.isNotBlank(Constants.PDMI_INTERFACE_URL_CRT_APP_FLAG)) {
                String[] split = Constants.PDMI_INTERFACE_URL_CRT_APP_FLAG.split(",");
                for (String crtAppFlag : split) {
                    jsonObject.put("mediaType", crtAppFlag);
                    logger.info("调用【获取根据crt机构id获取原创/转载数据】接口：" + Constants.PDMI_INTERFACE_HOST + Constants.PDMI_INTERFACE_URL_GET_CRT_INFO_BY_ADMIN_ID_AND_MEDIA_TYPE + "【请求参数为" + jsonObject.toString() + "】");
                    try {
                        String result = HttpUtils.doFormPost(jsonObject, Constants.PDMI_INTERFACE_HOST + Constants.PDMI_INTERFACE_URL_GET_CRT_INFO_BY_ADMIN_ID_AND_MEDIA_TYPE);
                        if (StringUtils.isNotBlank(result)) {
                            String returnCrtDataJsonArray = JSONObject.parseObject(result).get("data").toString();
                            List<CrtOriginAndTraceCountDTO> crtOriginAndTraceCountDTOs = JSONObject.parseArray(returnCrtDataJsonArray, CrtOriginAndTraceCountDTO.class);
                            if (crtOriginAndTraceCountDTOs != null) {
                                crtOriginAndTraceCountDTOResultList.addAll(crtOriginAndTraceCountDTOs);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            logger.info("由于未获取到crt和uar的appkey和mediaId的对应关系，所以无crt端的原创数和转载数！");
        }

        //4、数据封装--对之前产生的临时pv、uv数据整合crt的原创/转载数据进行整合
        List<StatisticAnalyseVO> statisticAnalyseVOList = new ArrayList<StatisticAnalyseVO>();
        for (StatisticAnalyseTmpVO statisticAnalyseTmpVO : statisticAnalyseTmpVOList) {
            //前台接口数据显示vo
            StatisticAnalyseVO statisticAnalyseVO = new StatisticAnalyseVO();
            //uar中pv和uv接口返回数据格式封装
            statisticAnalyseVO.setAppName(statisticAnalyseTmpVO.getAppName());
            statisticAnalyseVO.setPv(statisticAnalyseTmpVO.getPv());
            statisticAnalyseVO.setUv(statisticAnalyseTmpVO.getUv());
            //对crt中的原创数和转载数进行封装
            for (UarAppkeyAndCrtMediaIdDTO uarAppkeyAndCrtMediaIdDTO : uarAppkeyAndCrtMediaIdDTOList) {
                if (statisticAnalyseTmpVO.getUarAppkey().equals(uarAppkeyAndCrtMediaIdDTO.getUarMedia())) {
                    statisticAnalyseTmpVO.setCrtMediaId(uarAppkeyAndCrtMediaIdDTO.getCrtMedia());
                    for (CrtOriginAndTraceCountDTO crtOriginAndTraceCountDTO : crtOriginAndTraceCountDTOResultList) {
                        if (uarAppkeyAndCrtMediaIdDTO.getCrtMedia().equals(crtOriginAndTraceCountDTO.getMediaID())) {
                            statisticAnalyseTmpVO.setOriginCount(crtOriginAndTraceCountDTO.getOriginCount());
                            statisticAnalyseTmpVO.setTracedCount(crtOriginAndTraceCountDTO.getTracedCount());

                            statisticAnalyseVO.setOriginCount(statisticAnalyseTmpVO.getOriginCount());
                            statisticAnalyseVO.setTracedCount(statisticAnalyseTmpVO.getTracedCount());
                        }
                    }
                }
            }
            statisticAnalyseVOList.add(statisticAnalyseVO);
        }

        resultMap.put("code", 1);
        resultMap.put("data", statisticAnalyseVOList);
        resultMap.put("dataResponseTime", new SimpleDateFormat("yyyy-MM-dd").format(searchDate));
        return resultMap;
    }

    /**
     * 根据at和时间查询统计信息--web
     *
     * @param appkey        应用appkey
     * @param searchDateStr 查询时间字符串
     * @return
     */
    public UarStatisticWebAtDay selectPVAndUVByAtAndDateWeb(String appkey, String searchDateStr) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("at", appkey);
        params.put("day", searchDateStr);
        return uarStatisticWebAtDayMapper.selectPVAndUVByAtAndDateWeb(params);
    }

    /**
     * 根据at和时间查询统计信息--app
     *
     * @param appkey        应用appkey
     * @param searchDateStr 查询时间字符串
     * @return
     */
    public UarStatisticWebAtDay selectPVAndUVByAtAndDateApp(String appkey, String searchDateStr) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("at", appkey);
        params.put("day", searchDateStr);
        return uarStatisticWebAtDayMapper.selectPVAndUVByAtAndDateApp(params);
    }

    /**
     * 根据租户id查询指定应用类型的人们文章信息
     *
     * @param orginId          租户id
     * @param appFlag          应用标识(1:网站；2:客户端)
     * @param startPublishTime 查询起始时间(yyyyMMdd)
     * @param endPublishTime   查询截止时间(yyyyMMdd)
     * @return
     */
    public Map<String, Object> getHotArticleRank(String orginId, Integer appFlag, String startPublishTime, String endPublishTime, Integer topN) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (null == mediaService.findMediaByOrgId(orginId)) {
            resultMap.put("code", ErrorCode.ERROR_CODE_NOT_FOUND_MEDIA_ID);
            resultMap.put("message", "系统中不存在指定机构");
            return resultMap;
        }

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();


        List<Map<String, Object>> resultTitleAndUrlMapList = new ArrayList<Map<String, Object>>();
        Map<String, List<String>> appKeySetMap = new HashMap<String, List<String>>();
        switch (appFlag) {
            case 1:         //网站
//                appKeySet = uarBasicAppinfoService.getAppKeyByLesseeIdAndAppType(orginId, Constants.UAR_APP_TYPE_WEB);
//                logger.info("机构为【"+orginId+"】的网站appKey的集合为" + appKeySet);
//                if(null != appKeySet && appKeySet.size() > 0) {
//                    try {
//                        for(String appKey : appKeySet) {
//                            Set<String> appKeySetTmp = new HashSet<String>();
//                            appKeySetTmp.add(appKey);
//                            resultTitleAndUrlMapList = getTopNArticleInWebAndApp(appKeySetTmp, 1000, Long.parseLong(startPublishTime), Long.parseLong(endPublishTime), pageNum, pageSize);
//                        }
//                    } catch (Exception e) {
//                        logger.error(e);
//                    }
//                }

                appKeySetMap = uarBasicAppinfoService.findAppaccountListByOrgId(orginId, Constants.UAR_APP_TYPE_WEB);
                logger.info("appKey的集合为" + appKeySetMap.values().toString());
                if (null != appKeySetMap && appKeySetMap.size() > 0) {
                    try {
                        for (Map.Entry<String, List<String>> entries : appKeySetMap.entrySet()) {
                            String appName = entries.getKey();
                            List<String> appKeyList = entries.getValue();

                            Set<String> appKeySet = new HashSet<String>(appKeyList);
                            resultTitleAndUrlMapList = getTopNArticleInWebAndApp(appKeySet, Long.parseLong(startPublishTime), Long.parseLong(endPublishTime), topN);

                            Map<String, Object> resultStatisticTitleAndUrlMapList = new HashMap<String, Object>();

                            resultStatisticTitleAndUrlMapList.put("appName", appName);
                            resultStatisticTitleAndUrlMapList.put("articleList", resultTitleAndUrlMapList);

                            result.add(resultStatisticTitleAndUrlMapList);
                        }
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
                break;
            case 2:         //客户端
                appKeySetMap = uarBasicAppinfoService.findAppaccountListByOrgId(orginId, Constants.UAR_APP_TYPE_APP);
                logger.info("appKey的集合为" + appKeySetMap.values().toString());
                if (null != appKeySetMap && appKeySetMap.size() > 0) {
                    try {
                        for (Map.Entry<String, List<String>> entries : appKeySetMap.entrySet()) {
                            String appName = entries.getKey();
                            List<String> appKeyList = entries.getValue();

                            Set<String> appKeySet = new HashSet<String>(appKeyList);
                            resultTitleAndUrlMapList = getTopNArticleInWebAndApp(appKeySet, Long.parseLong(startPublishTime), Long.parseLong(endPublishTime), topN);

                            Map<String, Object> resultStatisticTitleAndUrlMapList = new HashMap<String, Object>();
                            resultStatisticTitleAndUrlMapList.put("appName", appName);
                            resultStatisticTitleAndUrlMapList.put("articleList", resultTitleAndUrlMapList);

                            result.add(resultStatisticTitleAndUrlMapList);
                        }
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
                break;
        }

        resultMap.put("code", 0);
        resultMap.put("data", result);
        return resultMap;
    }

    /**
     * 根据租户id查询指定应用类型的指定时间的总用户量
     *
     * @param orginId        租户id
     * @param appFlag        应用标识(1：网站；2：客户端)
     * @param searchBeginDay 查询起始天
     * @param searchEndDay   查询截止天
     * @return
     */
    public Map<String, Object> getTotalUserByOriginId(String orginId, Integer appFlag, Long searchBeginDay, Long searchEndDay) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Set<String> appKeyByLesseeIdAndAppType = new HashSet<String>();
        if (Constants.UAR_APP_TYPE_APP.equals(appFlag)) {
            appKeyByLesseeIdAndAppType = uarBasicAppinfoService.getAppKeyByLesseeIdAndAppType(orginId, Constants.UAR_APP_TYPE_APP);
        } else {
            appKeyByLesseeIdAndAppType = uarBasicAppinfoService.getAppKeyByLesseeIdAndAppType(orginId, Constants.UAR_APP_TYPE_WEB);
        }

        Map<String, Object> dataMap = new HashMap<String, Object>();

        if (null == appKeyByLesseeIdAndAppType || appKeyByLesseeIdAndAppType.size() == 0) {
            resultMap.put("code", 0);
            resultMap.put("data", dataMap);
            return resultMap;
        }
        logger.info("机构【" + orginId + "】的appKey的集合为" + appKeyByLesseeIdAndAppType.toString());

        Map<String, Long> totalUserMap = new HashMap<String, Long>();
        if (Constants.UAR_APP_TYPE_APP.equals(appFlag)) {
            //客户端的需要先查询截止日期的总用户，在查询开始日期的总用户数
            Long beginTotalUser = appActivityUserAtDayService.getTotalUserByAppKeySetAndSearchDay(appKeyByLesseeIdAndAppType, searchBeginDay);
            Long endTotalUser = appActivityUserAtDayService.getTotalUserByAppKeySetAndSearchDay(appKeyByLesseeIdAndAppType, searchEndDay);
            if (null == searchBeginDay || searchBeginDay == 0) {
                totalUserMap.put("totalUser", endTotalUser);
            } else {
                totalUserMap.put("totalUser", endTotalUser - beginTotalUser);
            }
        } else {
            totalUserMap.put("totalUser", webAtCLNDayService.getTotalUserByOriginId(appKeyByLesseeIdAndAppType, searchBeginDay, searchEndDay));
        }

        resultMap.put("code", 0);
        resultMap.put("data", totalUserMap);

        return resultMap;
    }



    private List<Map<String, Object>> getTopNArticleInWebAndApp(Set<String> atSet, Long from, Long to, Integer topN) {
        List<Map<String, Object>> titleUrlMapList = new ArrayList<Map<String, Object>>();

        TransportClient client = null;
        JSONObject resultJsonObj = new JSONObject();
        try {
            client = StatisticESClient.getClientIns();
        } catch (IOException e) {
            logger.error("es配置信息错误");
            e.printStackTrace();
            return titleUrlMapList;
        }
        try {
            TermsBuilder root1 = AggregationBuilders.terms("item_id")
                    .field("item_id").size(topN);          // fmyblack: size设为0才能获得准确值，但会损耗性能

            List<QueryBuilder> atQueryBuilderList = new ArrayList<QueryBuilder>();
            for (String appKey : atSet) {
                QueryBuilder queryBuilder = QueryBuilders.termQuery("at", appKey);
                atQueryBuilderList.add(queryBuilder);
            }
            SearchRequestBuilder srb = client
                    .prepareSearch(ESConfigConstants.ES_STATISTIC_INDEX_ITEM_STATISTIC)
                    .setTypes(ESConfigConstants.ES_STATISTIC_TYPE_ITEM_BASIC_STATISTIC)
                    .setQuery(
                            filterQuery(shouldQuery(atQueryBuilderList),
                                    dayRange(from, to)))
                    .addAggregation(
                            root1.subAggregation(sumAggs("pv", "pv"))
                                    .order(Terms.Order.aggregation("pv", false)))
                    .setSize(0);
            SearchResponse sr = srb.execute().actionGet();
            logger.info("查询聚合信息的pv排序=>" + srb.toString()
                    + "\r\n cost" + sr.getTook());

            Map<String, Aggregation> map = sr.getAggregations().asMap();
            StringTerms terms = (StringTerms) map.get("item_id");
            // 此处缓存部分结果，若查询为0条也缓存
            List<Terms.Bucket> resultList = terms.getBuckets();

            for (int i = 0; i < topN && i < resultList.size(); i++) {
                Terms.Bucket bucket = resultList.get(i);
                String key = (String) bucket.getKey();
                InternalSum pv = bucket.getAggregations().get("pv");
                int pvValue = (int) pv.getValue();
                logger.info("item_id:" + key + "====>pv:" + pvValue);
                // 修改为先走缓存获取内容详情
                Map<String, Object> subMap = getItemById(key);
                if (null != subMap && null != subMap.get("uri") && null != subMap.get("tt")) {
                    resultJsonObj = new JSONObject();
                    resultJsonObj.put("url", (String) subMap.get("uri"));
                    resultJsonObj.put("title", (String) subMap.get("tt"));
                    //暂时不需要下面两个字段
                    resultJsonObj.put("articleUniqueKey", key);
                    resultJsonObj.put("pv", pvValue);
//                    resultJsonObj.put("index", i+1);
                    titleUrlMapList.add(resultJsonObj);
                }
            }
        } catch (Exception e) {
            logger.error("查询topN文章数发生未知错误=》", e);
            e.printStackTrace();
        } finally {
            return titleUrlMapList;
        }
    }

    /**
     * 通过itemid获取内容详情
     *
     * @param itemId
     * @return
     */
    public Map<String, Object> getItemById(String itemId) {
        if (itemId == null || "".equals(itemId)) {
            return null;
        }
        Map<String, Object> subMap = new HashMap<String, Object>();
        try {
            Client client = StatisticESClient.getClientIns();
            SearchRequestBuilder srb = client.prepareSearch(ESConfigConstants.ES_STATISTIC_INDEX_ITEM_STATISTIC)
                    .setTypes(ESConfigConstants.ES_STATISTIC_TYPE_ITEM_BASIC_STATISTIC)
                    .setQuery(filterQuery(termQuery("item_id", itemId)))
                    .addSort("pv", SortOrder.DESC)
                    .setSize(1);
            logger.info("根据item_id查询文章的title和url信息：" + srb.toString());

            SearchResponse res = srb.execute().actionGet();
            System.out.println(res.getTookInMillis());
            subMap = res.getHits().iterator().next()
                    .getSource();
            return subMap;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("itemId为【" + itemId + "】查询标题和链接失败");
            return subMap;
        }
    }

    private QueryBuilder filterQuery(QueryBuilder... querys) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (QueryBuilder query : querys) {
            boolQuery.filter(query);
        }
        return boolQuery;
    }

    private static MetricsAggregationBuilder sumAggs(String name, String field) {
        return AggregationBuilders.sum(name).field(field);
    }

    // 查询时间范围
    private QueryBuilder dayRange(long from, long to) {
        return QueryBuilders.rangeQuery("day").gte(from).lte(to);
    }

    private static QueryBuilder termQuery(String name, String value) {
        return QueryBuilders.termQuery(name, value);
    }

    /*
     * 或查询
     */
    private static QueryBuilder shouldQuery(List<QueryBuilder> queryBuilderList) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (QueryBuilder query : queryBuilderList) {
            boolQuery.should(query);
        }
        boolQuery.minimumNumberShouldMatch(1);
        return boolQuery;
    }
}
