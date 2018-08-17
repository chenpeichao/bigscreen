package com.hubpd.bigscreen.service.uar_profile;

import java.util.Map;

/**
 * 用户分析--性别，年龄，地域
 *
 * @author cpc
 * @create 2018-08-11 20:07
 **/
public interface UserAnalyseService {
//    /**
//     * 用户分析接口，计算性别，青老中，前5地域
//     *
//     * @param orginId 机构id
//     * @return
//     */
//    public Map<String, Object> getUserAnalyse(String orginId);

    /**
     * 用户分析接口，计算性别，青老中，前5地域
     *
     * @param orginId 机构id
     * @return
     */
    public Map<String, Object> getUserAnalyseReturnData(String orginId);
}
