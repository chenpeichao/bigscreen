package com.hubpd.bigscreen.bean.uar_statistic;

public class AppAtDay extends AppAtDayKey {
    private Integer pv;

    private Integer uv;

    private Integer visit;

    private Float visittime;

    private Integer nUser;

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

    public Integer getnUser() {
        return nUser;
    }

    public void setnUser(Integer nUser) {
        this.nUser = nUser;
    }
}