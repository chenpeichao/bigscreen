package com.hubpd.bigscreen.service.common;

import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 获取用户分析数据任务service
 *
 * @author cpc
 * @create 2018-08-17 17:03
 **/
public interface TaskGetUserAnalyseService {
    /**
     * 用户分析接口，计算性别，青老中，前5地域
     *
     * @param orginId 机构id
     * @return
     */
    /**
     * 这里进行标注为异步任务，在执行此方法的时候，会单独开启线程来执行---但是此方法不能再本类调用
     */
    public Map<String, Object> getUserAnalyse(String orginId, Integer dataLevel);
}
