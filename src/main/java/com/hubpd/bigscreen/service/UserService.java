package com.hubpd.bigscreen.service;

import com.hubpd.bigscreen.bean.User;

/**
 * 用户
 * Created by ceek on 2018-08-08 22:26.
 */
public interface UserService {
    public User findOneById(Integer id);
}
