package com.hubpd.bigscreen.service.impl;

import com.hubpd.bigscreen.bean.User;
import com.hubpd.bigscreen.mapper.cluster.UserMapper;
import com.hubpd.bigscreen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户
 * Created by ceek on 2018-08-08 22:26.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    public User findOneById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
