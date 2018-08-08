package com.hubpd.bigscreen.service.uar_basic;

import com.hubpd.bigscreen.bean.uar_basic.UarBasicUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * uar系统用户service
 * Created by ceek on 2018-08-08 23:16.
 */
public interface UarUserService {
    /**
     * 根据机构id，查询uar系统中指定机构下所有用户信息
     * @param originId
     * @return
     */
    public List<UarBasicUser> findUarBasicUserListByOriginId(String originId);
}
