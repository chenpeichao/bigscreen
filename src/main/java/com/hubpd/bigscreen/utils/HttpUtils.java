package com.hubpd.bigscreen.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * http请求模拟工具类
 *
 * @author cpc
 * @create 2018-09-19 18:32
 **/
public class HttpUtils {
    public static Logger logger = Logger.getLogger(HttpUtils.class);

    public static String doGet(String url) {
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
     * post请求（用于请求表单格式的参数）
     *
     * @param paramsMap
     * @param url
     * @return
     */
    public static String doFormPost(Map<String, String> paramsMap, String url) {
        HttpClient httpclient = (HttpClient) HttpClients.createDefault(); //获取链接对象.
        HttpPost httpPost = new HttpPost(url); //创建表单.
        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();//用于存放表单数据.

        //遍历map 将其中的数据转化为表单数据
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        String jsonString = null;
        //对表单数据进行url编码
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs);
            httpPost.setEntity(urlEncodedFormEntity);
            HttpResponse response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                jsonString = EntityUtils.toString(responseEntity);
            } else {
                logger.info("请求返回:" + state + "(" + url + ")");
            }
        } catch (Exception e) {
            logger.error("调用请求失败【" + url + "】", e);
        } finally {
            return jsonString;
        }
    }
}
