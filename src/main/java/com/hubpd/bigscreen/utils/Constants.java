package com.hubpd.bigscreen.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 常量工具类
 * Created by ceek on 2018-08-09 22:45.
 */
@Component
public class Constants {
    /** 数据回溯指定天的数据--开始时间 */
    public static Integer DATA_BACK_BEGIN_DAY_NUM;
    /** 数据回溯指定天的数据--结束时间 */
    public static Integer DATA_BACK_END_DAY_NUM;

    @Value("${data_back_begin_day_num}")
    public void setDataBackBeginDayNum(Integer dataBackBeginDayNum) {
        this.DATA_BACK_BEGIN_DAY_NUM = dataBackBeginDayNum;
    }
    @Value("${data_back_end_day_num}")
    public void setDataBackEndDayNum(Integer dataBackEndDayNum) {
        this.DATA_BACK_END_DAY_NUM = dataBackEndDayNum;
    }
}
