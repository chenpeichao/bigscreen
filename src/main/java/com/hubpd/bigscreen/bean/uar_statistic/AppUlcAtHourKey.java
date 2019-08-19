package com.hubpd.bigscreen.bean.uar_statistic;

public class AppUlcAtHourKey {
    private String at;

    private Integer day;

    private Integer hour;

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

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }
}