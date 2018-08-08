package com.hubpd.bigscreen.service.uar_basic.impl;

import com.hubpd.bigscreen.bean.uar_basic.UarBasicUser;
import com.hubpd.bigscreen.mapper.uar_basic.UarBasicUserMapper;
import com.hubpd.bigscreen.service.uar_basic.UarUserService;
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
public class UarUserServiceImpl implements UarUserService{
    @Autowired
    private UarBasicUserMapper uarBasicUserMapper;

    /**
     * 根据机构id，查询uar系统中指定机构下所有用户信息
     * @param originId
     * @return
     */
    public List<UarBasicUser> findUarBasicUserListByOriginId(String originId) {
        return uarBasicUserMapper.findUarBasicUserListByOriginId(originId);
    }
}
