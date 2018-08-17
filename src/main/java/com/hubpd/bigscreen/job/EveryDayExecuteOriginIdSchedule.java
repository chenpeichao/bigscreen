package com.hubpd.bigscreen.job;

import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import com.hubpd.bigscreen.service.uar_profile.UserAnalyseService;
import com.hubpd.bigscreen.service.uar_profile.impl.UserAnalyseServiceImpl;
import com.hubpd.bigscreen.utils.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 每天3点定时缓存所有有效机构的用户分析信息到mysql数据库
 *
 * @author cpc
 * @create 2018-08-15 20:06
 **/
@Component
public class EveryDayExecuteOriginIdSchedule {
    private Logger logger = Logger.getLogger(EveryDayExecuteOriginIdSchedule.class);

    @Autowired
    private UarBasicUserService uarBasicUserService;
    @Autowired
    private UserAnalyseService userAnalyseService;

    @Scheduled(fixedRate = 1000 * 60 * 125)
//    @Scheduled(cron="0 0 3 * * ?")
    public void addTask() {
        List<String> allOriginIdList = uarBasicUserService.findAllOriginIdList();

        logger.info("定时任务缓存根据机构id缓存用户分析指定机构当天数据");

        for (String originId : allOriginIdList) {
            // 对用户分析接口进行调用，目的为了缓存当天接口返回值到mysql数据库
            try {

                Map<String, Object> userAnalyse = userAnalyseService.getUserAnalyse(originId);
                if (userAnalyse != null) {
                    logger.info("缓存根据机构id【" + originId + "】用户分析指定机构【" + DateUtils.getDateStrByDate(new Date(), "yyyy-MM-dd") + "】数据成功！");
                }
            } catch (Exception e) {
                logger.error("缓存根据机构id【" + originId + "】用户分析指定机构【" + DateUtils.getDateStrByDate(new Date(), "yyyy-MM-dd") + "】数据失败！", e);
            }
        }
    }
}