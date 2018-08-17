package com.hubpd.bigscreen.service.uar_basic;

import com.github.pagehelper.PageInfo;
import com.hubpd.bigscreen.bean.uar_basic.UarBasicUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * uar系统用户service
 * Created by ceek on 2018-08-08 23:16.
 */
public interface UarBasicUserService {
    /**
     * 根据机构id，查询uar系统中指定机构下所有用户信息
     * @param orginId         机构id
     * @return
     */
    public List<UarBasicUser> findUarBasicUserListByOrginId(String orginId);
    /**
     * 根据机构id，查询uar系统中指定机构下所有用户id列表信息
     * @param orginId         机构id
     * @return
     */
    public List<String> findUarBasicUserIdListByOrginId(String orginId);


    /**
     * 根据用户id列表，查询用户下对应的所有网站和移动应用的appaccount(即应用标识at)（返回结果为map<应用中文名，(应用appaccount，当为移动应用时为，Android和ios的appkey)>）
     *
     * @param userBasicUserIdList 用户id列表
     * @return
     */
    public Map<String, List<String>> findAppaccountListByUserBasicUserIdList(List<String> userBasicUserIdList);

    /**
     * 查询所有的机构信息
     *
     * @return
     */
    public List<String> findAllOriginIdList();
}
