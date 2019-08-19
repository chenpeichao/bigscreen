package com.hubpd.bigscreen.bean.uar_statistic;

public class WebAtClNDayKey {
    private String at;

    private String cl;

    private String n;

    private Integer day;

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at == null ? null : at.trim();
    }

    public String getCl() {
        return cl;
    }

    public void setCl(String cl) {
        this.cl = cl == null ? null : cl.trim();
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n == null ? null : n.trim();
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}