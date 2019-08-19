package com.hubpd.bigscreen.bean.uar_statistic;

public class AppActivityUserAtDay extends AppActivityUserAtDayKey {
    private Integer activityUser;

    private Integer totalUser;

    public Integer getActivityUser() {
        return activityUser;
    }

    public void setActivityUser(Integer activityUser) {
        this.activityUser = activityUser;
    }

    public Integer getTotalUser() {
        return totalUser;
    }

    public void setTotalUser(Integer totalUser) {
        this.totalUser = totalUser;
    }
}