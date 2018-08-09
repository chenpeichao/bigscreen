package com.hubpd.bigscreen.service.uar_basic.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hubpd.bigscreen.bean.uar_basic.UarBasicUser;
import com.hubpd.bigscreen.mapper.uar_basic.UarBasicUserMapper;
import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * uar系统用户service
 * Created by ceek on 2018-08-08 23:17.
 */
@Service
@Transactional
public class UarBasicUserServiceImpl implements UarBasicUserService {
    @Autowired
    private UarBasicUserMapper uarBasicUserMapper;

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
}
