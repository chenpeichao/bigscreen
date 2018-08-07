package com.hubpd.bigscreen.service.impl;

import com.hubpd.bigscreen.bean.Test;
import com.hubpd.bigscreen.mapper.TestMapper;
import com.hubpd.bigscreen.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 测试类
 * Created by ceek on 2018-08-07 23:15.
 */
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;

    public Test findOneById(Integer id) {
        return testMapper.selectByPrimaryKey(id);
    }
}
