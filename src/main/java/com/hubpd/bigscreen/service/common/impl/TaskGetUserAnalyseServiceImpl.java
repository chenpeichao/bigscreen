package com.hubpd.bigscreen.service.common.impl;

import com.alibaba.fastjson.JSON;
import com.hubpd.bigscreen.bean.origin_return.OriginReturnRecord;
import com.hubpd.bigscreen.bean.uar_profile.UserAnalyse;
import com.hubpd.bigscreen.dto.UserAreaCountDTO;
import com.hubpd.bigscreen.mapper.uar_profile.UarAtAreaMapper;
import com.hubpd.bigscreen.mapper.uar_profile.UserAnalyseMapper;
import com.hubpd.bigscreen.service.common.TaskGetUserAnalyseService;
import com.hubpd.bigscreen.service.origin_return.DicRegionService;
import com.hubpd.bigscreen.service.origin_return.OriginReturnRecordService;
import com.hubpd.bigscreen.service.origin_return.UarProfileBigscreenAreaDicService;
import com.hubpd.bigscreen.service.uar_basic.UarBasicAppinfoService;
import com.hubpd.bigscreen.utils.Constants;
import com.hubpd.bigscreen.utils.DateUtils;
import com.hubpd.bigscreen.vo.UserAnalyseRegionVO;
import com.hubpd.bigscreen.vo.UserAnalyseVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 获取用户分析数据任务service实现类
 *
 * @author cpc
 * @create 2018-08-17 17:03
 **/
@Service
public class TaskGetUserAnalyseServiceImpl implements TaskGetUserAnalyseService {
    private Logger logger = Logger.getLogger(TaskGetUserAnalyseServiceImpl.class);

    @Autowired
    private OriginReturnRecordService originReturnRecordService;
    @Autowired
    private DicRegionService dicRegionService;

    @Autowired
    private UserAnalyseMapper userAnalyseMapper;
    @Autowired
    private UarAtAreaMapper uarAtAreaMapper;
    @Autowired
    private UarProfileBigscreenAreaDicService uarProfileBigscreenAreaDicService;
    @Autowired
    private UarBasicAppinfoService uarBasicAppinfoService;

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
    /**
     * 这里进行标注为异步任务，在执行此方法的时候，会单独开启线程来执行---但是此方法不能在本类调用
     */
    @Async
    public Map<String, Object> getUserAnalyse(String orginId, Integer dataLevel) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String currentDateStr = DateUtils.getDateStrByDate(new Date(), "yyyy-MM-dd");

        // 首先从mysql数据库中查询数据，如果有数据，则直接返回，没有则进行es计算返回，并保存查询天和机构的数据到mysql数据库缓存
        String originReturnRecordStr = originReturnRecordService.findOriginReturnRecordByOriginIdAndDataLevel(orginId, currentDateStr, dataLevel);
        if (StringUtils.isNotBlank(originReturnRecordStr)) {
            resultMap.put("code", 1);
            resultMap.put("data", JSON.parse(originReturnRecordStr));
            return resultMap;
        }

        // 根据用户id列表，查询用户下对应的所有网站和移动应用的appaccount(即应用标识at)（返回结果为map<应用中文名，(应用appaccount，当为移动应用时为，Android和ios的appkey)>）
        Map<String, List<String>> appaccountMap = uarBasicAppinfoService.findAppaccountListByOrgId(orginId);

        // 对于机构对应的用户以及公众号进行打印
        logger.info("在【" + DateUtils.getDateStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "】查询机构id为【" + orginId + "】，对应应用信息为【" + appaccountMap.toString() + "】");

        List<UserAnalyseVO> userAnalyseVOList = new ArrayList<UserAnalyseVO>();

        for (String appName : appaccountMap.keySet()) {
            List<UserAnalyseRegionVO> userAnalyseRegionVOList = new ArrayList<UserAnalyseRegionVO>();

            logger.info("应用【" + appName + "】用户分析查询-------------开始");
            UserAnalyseVO userAnalyseVO = new UserAnalyseVO();
            //根据应用名称，获取应用的appacount(即应用的at)
            List<String> appaccountList = appaccountMap.get(appName);
            if (appaccountList != null && appaccountList.size() > 0) {
                Long maleNum = getTotalElements(appaccountList.get(0), "sex", "男");
                Long female = getTotalElements(appaccountList.get(0), "sex", "女");
                userAnalyseVO.getGender().put("female", female);
                userAnalyseVO.getGender().put("maleNum", maleNum);
                Long young = getTotalElements(appaccountList.get(0), "age", "青年");
                Long middleNum = getTotalElements(appaccountList.get(0), "age", "中年");
                Long oldNum = getTotalElements(appaccountList.get(0), "age", "老年");
                userAnalyseVO.getAge().put("young", young);
                userAnalyseVO.getAge().put("middleNum", middleNum);
                userAnalyseVO.getAge().put("oldNum", oldNum);

                List<String> allDicRegionName = dicRegionService.findAllDicRegionName();


                //遍历所有省份的信息
                if (dataLevel.equals(Constants.USER_PROFILE_REGIN_DATA_LEVEL_ES)) {
                    for (String regionName : allDicRegionName) {
                        UserAnalyseRegionVO userAnalyseRegionVO = new UserAnalyseRegionVO();
                        userAnalyseRegionVO.setRegionName(regionName);
                        //从画像es中获取数据
                        userAnalyseRegionVO.setNum(getTotalElements(appaccountList.get(0), "province", regionName));
                        userAnalyseRegionVOList.add(userAnalyseRegionVO);
                    }
                }

                //当有两个appkey时累加---即是移动应用有android和ios的appkey的区分
                if (appaccountList.size() > 1) {
                    Long maleNum1 = getTotalElements(appaccountList.get(1), "sex", "男");
                    Long female1 = getTotalElements(appaccountList.get(1), "sex", "女");
                    userAnalyseVO.getGender().put("maleNum", userAnalyseVO.getGender().get("maleNum") + maleNum1);
                    userAnalyseVO.getGender().put("female", userAnalyseVO.getGender().get("female") + female1);
                    Long young1 = getTotalElements(appaccountList.get(1), "age", "青年");
                    Long middleNum1 = getTotalElements(appaccountList.get(1), "age", "中年");
                    Long oldNum1 = getTotalElements(appaccountList.get(1), "age", "老年");
                    userAnalyseVO.getAge().put("young", userAnalyseVO.getAge().get("young") + young1);
                    userAnalyseVO.getAge().put("middleNum", userAnalyseVO.getAge().get("middleNum") + middleNum1);
                    userAnalyseVO.getAge().put("oldNum", userAnalyseVO.getAge().get("oldNum") + oldNum1);


                    //遍历所有省份的信息
                    if (dataLevel.equals(Constants.USER_PROFILE_REGIN_DATA_LEVEL_ES)) {
                        for (String regionName : allDicRegionName) {
                            Long provinceNum1 = getTotalElements(appaccountList.get(1), "province", regionName);
                            for (UserAnalyseRegionVO userAnalyseRegionVO : userAnalyseRegionVOList) {
                                if (userAnalyseRegionVO.getRegionName().equals(regionName)) {
                                    userAnalyseRegionVO.setNum(userAnalyseRegionVO.getNum() + provinceNum1);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (Constants.USER_PROFILE_REGIN_DATA_LEVEL_MYSQL.equals(dataLevel)) {
                //从spark跑ip信息的mysql中获取数据
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("appKeySet", appaccountList);
                List<UserAreaCountDTO> userCountInProvinceByAppKey = uarAtAreaMapper.findUserCountInProvinceByAppKey(paramMap);

                //查询用户画像地域字典表
                Map<String, String> allProfileProvinceAndShouNameDic = uarProfileBigscreenAreaDicService.getAllProfileProvinceAndShouNameDic();

                boolean flag = false;
                for (String regionNameFlag : allProfileProvinceAndShouNameDic.keySet()) {
                    UserAnalyseRegionVO userAnalyseRegionVO = new UserAnalyseRegionVO();
                    userAnalyseRegionVO.setRegionName(allProfileProvinceAndShouNameDic.get(regionNameFlag));

                    for (UserAreaCountDTO userAreaCountDTO : userCountInProvinceByAppKey) {
                        if (userAreaCountDTO.getRegionName().equals(regionNameFlag)) {
                            //从画像es中获取数据
                            userAnalyseRegionVO.setNum(userAreaCountDTO.getNum());
                            flag = true;
                        }
                    }
                    if (!flag) {
                        userAnalyseRegionVO.setNum(0l);
                    }
                    flag = false;

                    userAnalyseRegionVOList.add(userAnalyseRegionVO);
                }
            }

            userAnalyseRegionVOList.sort(new Comparator<UserAnalyseRegionVO>() {
                @Override
                public int compare(UserAnalyseRegionVO o1, UserAnalyseRegionVO o2) {
                    Long tmp = o2.getNum() - o1.getNum();
                    return tmp.intValue();
                }
            });
            userAnalyseVO.setRegion(userAnalyseRegionVOList.subList(0, userAnalyseRegionVOList.size() > 5 ? 5 : userAnalyseRegionVOList.size()));

            //对于dataLevel为从mysql中获取是，重新赋值地域信息

            logger.info("应用【" + appName + "】用户分析查询-------------结束");
            userAnalyseVO.setAppName(appName);
            userAnalyseVOList.add(userAnalyseVO);
        }

        logger.info("机构id为【" + orginId + "】的数据打印完成");
        // 对于指定机构在指定查询日期的返回数据，进行mysql数据库的缓存
        String returnDate = originReturnRecordService.findOriginReturnRecordByOriginIdDataLevel(orginId, currentDateStr, dataLevel);
        if (StringUtils.isBlank(returnDate)) {
            OriginReturnRecord originReturnRecord = new OriginReturnRecord();
            originReturnRecord.setOriginId(orginId);
            originReturnRecord.setReturnDate(currentDateStr);
            originReturnRecord.setReturnJson(JSON.toJSONString(userAnalyseVOList));
            originReturnRecord.setDataLevel(dataLevel);

            //接口返回记录保存mysql，缓存
            originReturnRecordService.insert(originReturnRecord);
        }

        resultMap.put("code", 1);
        resultMap.put("data", userAnalyseVOList);
        return resultMap;
    }

    /**
     * 根据指定appkey以及指定字段查询es中的总数据量
     *
     * @param at         appkey应用标识
     * @param queryField 待查询es标识
     * @param queryParam 查询参数值
     * @return
     */
    public Long getTotalElements(String at, String queryField, String queryParam) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.termQuery(queryField, queryParam));
        builder.must(QueryBuilders.queryStringQuery("at : " + at));
        builder.must(QueryBuilders.rangeQuery("tag_count").gt(0));


//        FieldSortBuilder sort = SortBuilders.fieldSort("age").order(SortOrder.DESC);
        //设置分页
        //====注意!es的分页和Hibernate一样api是从第0页开始的=========
//        PageRequest page = new PageRequest(0, 2);
//
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        //将搜索条件设置到构建中
//        nativeSearchQueryBuilder.withQuery(builder);
//        //将分页设置到构建中
//        nativeSearchQueryBuilder.withPageable(page);

        //将排序设置到构建中
//        nativeSearchQueryBuilder.withSort(sort);
        //生产NativeSearchQuery
        ;
        NativeSearchQuery query = nativeSearchQueryBuilder.withFilter(builder).build();
//        NativeSearchQuery query = nativeSearchQueryBuilder.build();

        //执行,返回包装结果的分页
        Page<UserAnalyse> resutlList = userAnalyseMapper.search(query);
        return resutlList.getTotalElements();
    }
//    /**
//     * 根据指定appkey以及指定字段查询es中的总数据量
//     *
//     * @param at         appkey应用标识
//     * @param queryField 待查询es标识
//     * @param queryParam 查询参数值
//     * @return
//     */
//    public Long getTotalElements(String at, String queryField, String queryParam) {
//        BoolQueryBuilder builder = QueryBuilders.boolQuery();
//        builder.must(QueryBuilders.termQuery(queryField, queryParam));
//        builder.must(QueryBuilders.queryStringQuery("at : " + at));
//        builder.must(QueryBuilders.rangeQuery("tag_count").gt(0));
//
////        FieldSortBuilder sort = SortBuilders.fieldSort("age").order(SortOrder.DESC);
//        //设置分页
//        //====注意!es的分页和Hibernate一样api是从第0页开始的=========
//        PageRequest page = new PageRequest(0, 2);
//
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        //将搜索条件设置到构建中
//        nativeSearchQueryBuilder.withQuery(builder);
//        //将分页设置到构建中
//        nativeSearchQueryBuilder.withPageable(page);
//
//        //将排序设置到构建中
////        nativeSearchQueryBuilder.withSort(sort);
//        //生产NativeSearchQuery
//        NativeSearchQuery query = nativeSearchQueryBuilder.build();
//        //执行,返回包装结果的分页
//        Page<UserAnalyse> resutlList = userAnalyseMapper.search(query);
//        return resutlList.getTotalElements();
//    }
}
