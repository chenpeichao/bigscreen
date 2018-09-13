package com.hubpd.bigscreen.service.statistic_analyse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hubpd.bigscreen.service.statistic_analyse.StatisticAnalyseService;
import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import com.hubpd.bigscreen.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 根据机构id和查询时间查询pv、uv以及crt的相关原创数和转载数---默认查询昨天
     *
     * @param orginIdStr
     * @param searchDate
     * @return
     */
    public Map<String, Object> getStatisticAnalyse(String orginIdStr, Date searchDate) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        //1、首先查询传递的机构id，是否在uar中是有效的机构id
//        List<String> allOriginIdListInUar = uarBasicUserService.findAllOriginIdListInUar();
//        if (!allOriginIdListInUar.contains(orginIdStr)) {
//            //传递的机构id在uar中不存在时，返回null数据
//            resultMap.put("code", 0);
//            resultMap.put("message", "系统中不存在此机构id");
//            return resultMap;
//        }

        //根据uar的机构id获取crt机构id的url请求封装
        String getCrtAdminIdByUarOrgIdUrl = Constants.PDMI_INTERFACE_HOST + Constants.PDMI_INTERFACE_URL_GET_ADMIN_ID_BY_UAR_ORG_ID + "?" + "uar=" + orginIdStr;
        logger.info("调用【获取uar和crt机构id对应关系】接口：" + getCrtAdminIdByUarOrgIdUrl);
        String returnJson = this.testGet(getCrtAdminIdByUarOrgIdUrl);
        if (StringUtils.isNotBlank(returnJson)) {
            Map<String, String> parse = (Map<String, String>) JSON.parse(returnJson);
            //获取到crt的机构id
            String crtAdminId = parse.get("crt");
        } else {
            resultMap.put("code", 0);
            resultMap.put("message", "获取原创转载数信息失败，请重试！");
        }

        return resultMap;
    }

    public String testGet(String url) {
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
            e.printStackTrace();
        } finally {
            try {
                httpCilent2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultJson;
        }
    }
}
