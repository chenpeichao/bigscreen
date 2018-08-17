package com.hubpd.bigscreen.service.uar_basic.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hubpd.bigscreen.bean.uar_basic.UarBasicAppinfo;
import com.hubpd.bigscreen.bean.uar_basic.UarBasicTaskOrgin;
import com.hubpd.bigscreen.bean.uar_basic.UarBasicUser;
import com.hubpd.bigscreen.mapper.uar_basic.UarBasicTaskOrginMapper;
import com.hubpd.bigscreen.mapper.uar_basic.UarBasicUserAppMapper;
import com.hubpd.bigscreen.mapper.uar_basic.UarBasicUserMapper;
import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * uar系统用户service
 * Created by ceek on 2018-08-08 23:17.
 */
@Service
@Transactional
public class UarBasicUserServiceImpl implements UarBasicUserService {
    @Autowired
    private UarBasicUserMapper uarBasicUserMapper;
    @Autowired
    private UarBasicUserAppMapper uarBasicUserAppMapper;
    @Autowired
    private UarBasicTaskOrginMapper uarBasicTaskOrginMapper;

    /**
     * 根据机构id，查询uar系统中指定机构下所有用户信息
     * @param orginId         机构id
     * @return
     */
    public List<UarBasicUser> findUarBasicUserListByOrginId(String orginId) {
        return uarBasicUserMapper.findUarBasicUserListByOrginId(orginId);
    }

    /**
     * 根据机构id，查询uar系统中指定机构下所有用户id列表信息
     * @param orginId         机构id
     * @return
     */
    public List<String> findUarBasicUserIdListByOrginId(String orginId) {
        return uarBasicUserMapper.findUarBasicUserIdListByOrginId(orginId);
    }

    /**
     * 根据用户id列表，查询用户下对应的所有网站和移动应用的appaccount(即应用标识at)（返回结果为map<应用中文名，(应用appaccount，当为移动应用时为，Android和ios的appkey)>）
     *
     * @param userBasicUserIdList 用户id列表
     * @return
     */
    public Map<String, List<String>> findAppaccountListByUserBasicUserIdList(List<String> userBasicUserIdList) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userBasicUserIdList", userBasicUserIdList);
        if (userBasicUserIdList != null && userBasicUserIdList.size() > 0) {
            List<UarBasicAppinfo> appaccountListByUserBasicUserIdList = uarBasicUserAppMapper.findAppaccountListByUserBasicUserIdList(params);
            for (UarBasicAppinfo uarBasicAppinfo : appaccountListByUserBasicUserIdList) {
                List<String> resultAppaccountList = new ArrayList<String>();
                //因为当为移动应用时，可能存在Android和ios的区别，有两个appaccount，所以用list封装
                if (StringUtils.isNotBlank(uarBasicAppinfo.getAppaccount())) {
                    resultAppaccountList.add(uarBasicAppinfo.getAppaccount());
                }
                if (StringUtils.isNotBlank(uarBasicAppinfo.getAppaccount2())) {
                    resultAppaccountList.add(uarBasicAppinfo.getAppaccount2());
                }
                result.put(uarBasicAppinfo.getAppname(), resultAppaccountList);
            }
        }
        return result;
    }

    /**
     * 查询所有的机构信息---大屏缓存
     *
     * @return
     */
    public List<String> findAllOriginIdListInBigscreen() {
        return uarBasicTaskOrginMapper.findAllOriginIdListInBigscreen();
    }

    /**
     * 根据机构id查询大屏缓存的机构信息
     *
     * @param orginId 机构id
     * @return
     */
    public List<UarBasicTaskOrgin> findTaskOriginByOriginIdInBigscreen(String orginId) {
        return uarBasicTaskOrginMapper.findTaskOriginByOriginIdInBigscreen(orginId);
    }

    /**
     * 保存大屏系统中机构信息
     *
     * @param uarBasicTaskOrgin 机构id相关信息
     * @return
     */
    public void saveTaskOrgin(UarBasicTaskOrgin uarBasicTaskOrgin) {
        uarBasicTaskOrginMapper.insertSelective(uarBasicTaskOrgin);
    }

    /**
     * 查询所有的机构id列表信息，uar系统本身存在
     *
     * @return
     */
    public List<String> findAllOriginIdListInUar() {
        return uarBasicUserMapper.findAllOriginIdListInUar();
    }
}
