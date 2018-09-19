package com.hubpd.bigscreen.service.statistic_analyse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hubpd.bigscreen.bean.uar_basic.UarBasicAppinfo;
import com.hubpd.bigscreen.bean.uar_statistic.UarStatisticWebAtDay;
import com.hubpd.bigscreen.dto.CrtOriginAndTraceCountDTO;
import com.hubpd.bigscreen.dto.UarAppkeyAndCrtMediaIdDTO;
import com.hubpd.bigscreen.mapper.uar_statistic.UarStatisticWebAtDayMapper;
import com.hubpd.bigscreen.service.statistic_analyse.StatisticAnalyseService;
import com.hubpd.bigscreen.service.uar_basic.UarBasicAppinfoService;
import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import com.hubpd.bigscreen.utils.Constants;
import com.hubpd.bigscreen.utils.DateUtils;
import com.hubpd.bigscreen.utils.HttpUtils;
import com.hubpd.bigscreen.utils.Md5Utils;
import com.hubpd.bigscreen.vo.StatisticAnalyseTmpVO;
import com.hubpd.bigscreen.vo.StatisticAnalyseVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UarStatisticWebAtDayMapper uarStatisticWebAtDayMapper;

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
}
