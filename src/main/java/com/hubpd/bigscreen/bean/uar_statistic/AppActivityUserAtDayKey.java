package com.hubpd.bigscreen.bean.uar_statistic;

public class AppActivityUserAtDayKey {
    private String at;

    private Integer day;

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at == null ? null : at.trim();
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}