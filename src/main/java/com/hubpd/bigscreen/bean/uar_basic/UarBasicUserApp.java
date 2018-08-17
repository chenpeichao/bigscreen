package com.hubpd.bigscreen.bean.uar_basic;

import java.util.Date;

public class UarBasicUserApp {
    private String userid;

    private String appid;

    private Short apptype;

    private String columnid;

    private Boolean isWhole;

    private Boolean mode;

    private Boolean type;

    private Integer groupid;

    private Boolean status;

    private Date atime;

    private String aid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    public Short getApptype() {
        return apptype;
    }

    public void setApptype(Short apptype) {
        this.apptype = apptype;
    }

    public String getColumnid() {
        return columnid;
    }

    public void setColumnid(String columnid) {
        this.columnid = columnid == null ? null : columnid.trim();
    }

    public Boolean getIsWhole() {
        return isWhole;
    }

    public void setIsWhole(Boolean isWhole) {
        this.isWhole = isWhole;
    }

    public Boolean getMode() {
        return mode;
    }

    public void setMode(Boolean mode) {
        this.mode = mode;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getAtime() {
        return atime;
    }

    public void setAtime(Date atime) {
        this.atime = atime;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid == null ? null : aid.trim();
    }
}