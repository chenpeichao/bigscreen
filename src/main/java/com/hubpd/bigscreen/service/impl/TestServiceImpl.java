package com.hubpd.bigscreen.service.impl;

import com.hubpd.bigscreen.bean.Test;
import com.hubpd.bigscreen.mapper.master.TestMapper;
import com.hubpd.bigscreen.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 测试类
 * Created by ceek on 2018-08-07 23:15.
 */
@Service
//注解会生效
@Transactional
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;

    public Test findOneById(Integer id) {
        testMapper.deleteByPrimaryKey(3);
        int i = 1/0;
        Test test = new Test();
        test.setUserId(7);
        test.setOrderDate(new Date());
        testMapper.insert(test);
        return testMapper.selectByPrimaryKey(id);
    }
}
