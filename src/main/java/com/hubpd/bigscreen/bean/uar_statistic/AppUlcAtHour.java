package com.hubpd.bigscreen.bean.uar_statistic;

public class AppUlcAtHour extends AppUlcAtHourKey {
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