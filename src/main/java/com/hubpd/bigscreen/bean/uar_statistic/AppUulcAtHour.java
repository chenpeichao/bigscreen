package com.hubpd.bigscreen.bean.uar_statistic;

public class AppUulcAtHour extends AppUulcAtHourKey {
    private Integer nUser;

    private Integer activityUser;

    public Integer getnUser() {
        return nUser;
    }

    public void setnUser(Integer nUser) {
        this.nUser = nUser;
    }

    public Integer getActivityUser() {
        return activityUser;
    }

    public void setActivityUser(Integer activityUser) {
        this.activityUser = activityUser;
    }
}