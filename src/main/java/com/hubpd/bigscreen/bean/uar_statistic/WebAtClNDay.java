package com.hubpd.bigscreen.bean.uar_statistic;

public class WebAtClNDay extends WebAtClNDayKey {
    private Integer pv;

    private Integer uv;

    private Integer visit;

    private Float visittime;

    private Integer exitVisit;

    private Float visitcount;

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Integer getVisit() {
        return visit;
    }

    public void setVisit(Integer visit) {
        this.visit = visit;
    }

    public Float getVisittime() {
        return visittime;
    }

    public void setVisittime(Float visittime) {
        this.visittime = visittime;
    }

    public Integer getExitVisit() {
        return exitVisit;
    }

    public void setExitVisit(Integer exitVisit) {
        this.exitVisit = exitVisit;
    }

    public Float getVisitcount() {
        return visitcount;
    }

    public void setVisitcount(Float visitcount) {
        this.visitcount = visitcount;
    }
}