package com.hubpd.bigscreen.service.statistic_analyse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hubpd.bigscreen.bean.uar_statistic.UarStatisticWebAtDay;
import com.hubpd.bigscreen.dto.CrtOriginAndTraceCountDTO;
import com.hubpd.bigscreen.dto.UarAppkeyAndCrtMediaIdDTO;
import com.hubpd.bigscreen.mapper.uar_statistic.UarStatisticWebAtDayMapper;
import com.hubpd.bigscreen.service.statistic_analyse.StatisticAnalyseService;
import com.hubpd.bigscreen.service.uar_basic.UarBasicAppinfoService;
import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import com.hubpd.bigscreen.utils.Constants;
import com.hubpd.bigscreen.utils.DateUtils;
import com.hubpd.bigscreen.utils.Md5Utils;
import com.hubpd.bigscreen.vo.StatisticAnalyseVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

        //1、首先查询传递的机构id，是否在uar中是有效的机构id
//        List<String> allOriginIdListInUar = uarBasicUserService.findAllOriginIdListInUar();
//        if (!allOriginIdListInUar.contains(orginIdStr)) {
//            //传递的机构id在uar中不存在时，返回null数据
//            resultMap.put("code", 0);
//            resultMap.put("message", "系统中不存在此机构id");
//            return resultMap;
//        }

        // 1、根据用户id列表，查询用户下对应的所有网站和移动应用的appaccount(即应用标识at)（返回结果为map<应用中文名，(应用appaccount，当为移动应用时为，Android和ios的appkey)>）
        Map<String, List<String>> appaccountMap = uarBasicAppinfoService.findAppaccountListByOrgId(orginIdStr);
        // 对于机构对应的用户以及公众号进行打印
        logger.info("在【" + DateUtils.getDateStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "】查询机构id为【" + orginIdStr + "】，对应应用信息为【" + appaccountMap.toString() + "】");

        List<StatisticAnalyseVO> statisticAnalyseVOList = new ArrayList<StatisticAnalyseVO>();

        //2、通过应用appkey获取pv和uv数据
        for (String appName : appaccountMap.keySet()) {
            StatisticAnalyseVO statisticAnalyseVO = new StatisticAnalyseVO();

            //根据应用名称，获取应用的appacount(即应用的at)
            List<String> appaccountList = appaccountMap.get(appName);
            statisticAnalyseVO.setAppName(appName);
            for (String appaccount : appaccountList) {
                statisticAnalyseVO.setUarAppkey(StringUtils.isNotBlank(statisticAnalyseVO.getUarAppkey()) ? statisticAnalyseVO.getUarAppkey() + "|" + appaccount : appaccount);
                UarStatisticWebAtDay uarStatisticWebAtDay = this.selectPVAndUVByAtAndDate(appaccount, searchDateStr);
                if (uarStatisticWebAtDay != null) {
                    statisticAnalyseVO.setPv(statisticAnalyseVO.getPv() + uarStatisticWebAtDay.getPv());
                    statisticAnalyseVO.setUv(statisticAnalyseVO.getUv() + uarStatisticWebAtDay.getUv());
                }
            }

            statisticAnalyseVOList.add(statisticAnalyseVO);
        }

        //-----------------crt信息获取--------------------------
        //1、根据uar的机构id获取crt机构id的url请求封装
        String crtAdminId = "";         //crt机构id
        String getCrtAdminIdByUarOrgIdUrl = Constants.PDMI_INTERFACE_HOST + Constants.PDMI_INTERFACE_URL_GET_ADMIN_ID_BY_UAR_ORG_ID + "?" + "uar=" + orginIdStr;
        logger.info("调用【获取uar和crt机构id对应关系】接口：" + getCrtAdminIdByUarOrgIdUrl);
        String returnJsonGetCrtAdminIdByUarOrgIdUrl = this.doGet(getCrtAdminIdByUarOrgIdUrl);
        if (StringUtils.isNotBlank(returnJsonGetCrtAdminIdByUarOrgIdUrl)) {
            Map<String, String> parseGetCrtAdminIdByUarOrgIdUrl = (Map<String, String>) JSON.parse(returnJsonGetCrtAdminIdByUarOrgIdUrl);
            //获取到crt的机构id
            crtAdminId = parseGetCrtAdminIdByUarOrgIdUrl.get("crt");
        } else {
            logger.info("用户调用的机构id【" + orginIdStr + "】未获取到crt端的机构id---first");
            resultMap.put("code", 1);
            resultMap.put("data", statisticAnalyseVOList);
            return resultMap;
        }

        //2、根据crt机构id，获取uar的appkey和crt的mediaId的对应关系数据
        List<UarAppkeyAndCrtMediaIdDTO> uarAppkeyAndCrtMediaIdDTOList = new ArrayList<UarAppkeyAndCrtMediaIdDTO>();
        String getAppKeyAndMediaIdRelationUrl = Constants.PDMI_INTERFACE_HOST + Constants.PDMI_INTERFACE_URL_CRT_MEDIA_RELATION_BY_UAR_ORG_ID + "?" + "uar=" + orginIdStr;
        logger.info("调用【获取uar的appkey和crt的mediaId对应关系】接口：" + getAppKeyAndMediaIdRelationUrl);
        String returnJsonGetAppKeyAndMediaIdRelationUrl = this.doGet(getAppKeyAndMediaIdRelationUrl);
        if (StringUtils.isNotBlank(returnJsonGetAppKeyAndMediaIdRelationUrl)) {
            uarAppkeyAndCrtMediaIdDTOList = JSON.parseArray(returnJsonGetAppKeyAndMediaIdRelationUrl, UarAppkeyAndCrtMediaIdDTO.class);
        } else {
            logger.info("用户调用的机构id【" + orginIdStr + "】未获取到crt端的机构id---second");
            resultMap.put("code", 1);
            resultMap.put("data", statisticAnalyseVOList);
            return resultMap;
        }

        //3、根据crt机构id，查询网站和app的相关原创和转载数据
        List<CrtOriginAndTraceCountDTO> crtOriginAndTraceCountDTOResultList = new ArrayList<CrtOriginAndTraceCountDTO>();
        if (StringUtils.isNotBlank(crtAdminId) && uarAppkeyAndCrtMediaIdDTOList.size() > 0) {
//            JSONObject jsonObject = new JSONObject();
            Long timesMillis = System.currentTimeMillis();
//            jsonObject.put("clientCode", Constants.CLIENT_CODE);
//            jsonObject.put("secretCode", Md5Utils.getMD5OfStr(Constants.CLIENT_CODE + Constants.SECRET_KEY + timesMillis));
//            jsonObject.put("timesMillis", timesMillis);
//            jsonObject.put("adminId", timesMillis);
//            jsonObject.put("mediaType", timesMillis);
//
//            String result = this.doPost(Constants.PDMI_INTERFACE_HOST+ Constants.PDMI_INTERFACE_URL_GET_CRT_INFO_BY_ADMIN_ID_AND_MEDIA_TYPE, jsonObject.toJSONString());
//
//            if(StringUtils.isNotBlank(result)) {
//                Map<String, String> parseGetCrtAdminIdByUarOrgIdUrl = (Map<String, String>) JSON.parse(result);
//                //获取crt中的指定机构下的应用的网站或app原创数和转载数
//                String dataOrginAndTranceReturnJsonStr = parseGetCrtAdminIdByUarOrgIdUrl.get("data");
//                if(StringUtils.isNotBlank(dataOrginAndTranceReturnJsonStr)) {
//                    crtOriginAndTraceCountDTOResultList.addAll(JSON.parseArray(dataOrginAndTranceReturnJsonStr, CrtOriginAndTraceCountDTO.class));
//                }
//            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("clientCode", "caiyun_lanxinwen");
            jsonObject.put("timesMillis", "1535532403032");
            jsonObject.put("secretCode", "6B0544190295A25E3AB0F80F79AF2059");
            jsonObject.put("adminId", crtAdminId);
            jsonObject.put("mediaType", 0);
            System.out.println("请求内容" + jsonObject.toJSONString());
            try {
                String s = doPost1(Constants.PDMI_INTERFACE_HOST + Constants.PDMI_INTERFACE_URL_GET_CRT_INFO_BY_ADMIN_ID_AND_MEDIA_TYPE, jsonObject.toJSONString());
                System.out.println(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("获取crt机构id失败，未获取到crt和uar的appkey和mediaId的对应关系");
            resultMap.put("code", 1);
            resultMap.put("data", statisticAnalyseVOList);
            return resultMap;
        }


//        if (StringUtils.isNotBlank(crtAdminId)) {
//            //2、根据crt的机构id以及媒体类型(0：网站；2：app)获取crt机构下的指定媒体类型的应用的原创数和转载数
//            String getCrtOrginAndTraceCountByCrtOrgIdUrlNet = Constants.PDMI_INTERFACE_HOST + Constants.PDMI_INTERFACE_URL_GET_CRT_INFO_BY_ADMIN_ID_AND_MEDIA_TYPE
//                    + "?clientCode=shunyibigscreen&secretCode=ABEED324ABF6E81EAF45AC14BFB69B1E&timesMillis=1523931112228"
//                    + "&adminId=" + crtAdminId
//                    + "&mediaType=0";
//            String returnJsonGetCrtOrginAndTraceCountByCrtOrgIdUrlNet = this.doGet(getCrtOrginAndTraceCountByCrtOrgIdUrlNet);
//            if (StringUtils.isNotBlank(returnJsonGetCrtOrginAndTraceCountByCrtOrgIdUrlNet)) {
//                Map<String, String> parseGetCrtOrginAndTraceCountByCrtOrgIdUrlNet = (Map<String, String>) JSON.parse(returnJsonGetCrtOrginAndTraceCountByCrtOrgIdUrlNet);
//                //解析网站的原创和转载数
//                String crtOrgAndTraceCount = parseGetCrtOrginAndTraceCountByCrtOrgIdUrlNet.get("data");
//                if (StringUtils.isNotBlank(crtOrgAndTraceCount)) {
//                    List<CrtOriginAndTraceCountDTO> crtOriginAndTraceCountDTOs = JSON.parseArray(crtOrgAndTraceCount, CrtOriginAndTraceCountDTO.class);
//                    for (CrtOriginAndTraceCountDTO crtOriginAndTraceCountDTO : crtOriginAndTraceCountDTOs) {
//                        for (StatisticAnalyseVO statisticAnalyseVO : statisticAnalyseVOList) {
//                        }
//                    }
//                } else {
//                    logger.info("用户调用的机构id【" + orginIdStr + "】未获取到crt端网站的原创数和转载数");
//                    resultMap.put("code", 1);
//                    resultMap.put("data", statisticAnalyseVOList);
//                }
//            } else {
//                logger.info("用户调用的机构id【" + orginIdStr + "】未获取到crt端网站的原创数和转载数");
//                resultMap.put("code", 1);
//                resultMap.put("data", statisticAnalyseVOList);
//            }
//            String getCrtOrginAndTraceCountByCrtOrgIdUrlApp = Constants.PDMI_INTERFACE_HOST + Constants.PDMI_INTERFACE_URL_GET_CRT_INFO_BY_ADMIN_ID_AND_MEDIA_TYPE
//                    + "?clientCode=shunyibigscreen&secretCode=ABEED324ABF6E81EAF45AC14BFB69B1E&timesMillis=1523931112228"
//                    + "&adminId=" + crtAdminId
//                    + "&mediaType=2";
//        } else {
//            logger.info("用户调用的机构id【" + orginIdStr + "】未获取到crt端的机构id");
//            resultMap.put("code", 1);
//            resultMap.put("data", statisticAnalyseVOList);
//        }

        resultMap.put("code", 1);
        resultMap.put("data", statisticAnalyseVOList);
        return resultMap;
    }

    /**
     * post请求（用于请求json格式的参数）
     *
     * @param url
     * @param params
     * @return
     */
    public String doPost1(String url, String params) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
//        httpPost.setHeader("Accept", "application/json");
//        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                System.out.println("请求成功！！");
                return jsonString;
            } else {
                System.err.println("请求返回:" + state + "(" + url + ")");
            }
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String doGet(String url) {
        CloseableHttpClient httpCilent2 = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(20000)   //设置连接超时时间
                .setConnectionRequestTimeout(20000) // 设置请求超时时间
                .setSocketTimeout(20000)
                .setRedirectsEnabled(true)//默认允许自动重定向
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        String resultJson = null;
        try {
            HttpResponse httpResponse = httpCilent2.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                resultJson = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果
            }
        } catch (IOException e) {
            logger.error("调用请求失败【" + url + "】", e);
        } finally {
            try {
                httpCilent2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultJson;
        }
    }

    /**
     * post请求（用于请求json格式的参数）
     *
     * @param url
     * @param params
     * @return
     */
    private String doPost(String url, String params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
//        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {

            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                return jsonString;
            } else {
                logger.error("接口调用返回失败:" + state + "(" + url + ")");
            }
        } catch (Exception e) {
            logger.error("接口调用失败：【" + url + "】", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据at和时间查询统计信息
     *
     * @param appkey        应用appkey
     * @param searchDateStr 查询时间字符串
     * @return
     */
    public UarStatisticWebAtDay selectPVAndUVByAtAndDate(String appkey, String searchDateStr) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("at", appkey);
        params.put("day", searchDateStr);
        return uarStatisticWebAtDayMapper.selectPVAndUVByAtAndDate(params);
    }
}
