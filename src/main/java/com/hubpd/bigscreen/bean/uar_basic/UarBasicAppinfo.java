package com.hubpd.bigscreen.bean.uar_basic;

import java.util.Date;

public class UarBasicAppinfo {
    private Integer appid;

    private Integer apptype;

    private String appsubtype;

    private String appname;

    private String domain;

    private String appmedia;

    private Integer apptag;

    private String applog;

    private String appaccount;

    private String appaccount2;

    private String appdesc;

    private Boolean status;

    private Date atime;

    private String aid;

    private Date mtime;

    private String mid;

    private Date deletedAt;

    private String subDomainRegex;

    private String appscode;

    private String lang;

    private Integer mediaId;

    private String containDomain;

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public Integer getApptype() {
        return apptype;
    }

    public void setApptype(Integer apptype) {
        this.apptype = apptype;
    }

    public String getAppsubtype() {
        return appsubtype;
    }

    public void setAppsubtype(String appsubtype) {
        this.appsubtype = appsubtype == null ? null : appsubtype.trim();
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname == null ? null : appname.trim();
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain == null ? null : domain.trim();
    }

    public String getAppmedia() {
        return appmedia;
    }

    public void setAppmedia(String appmedia) {
        this.appmedia = appmedia == null ? null : appmedia.trim();
    }

    public Integer getApptag() {
        return apptag;
    }

    public void setApptag(Integer apptag) {
        this.apptag = apptag;
    }

    public String getApplog() {
        return applog;
    }

    public void setApplog(String applog) {
        this.applog = applog == null ? null : applog.trim();
    }

    public String getAppaccount() {
        return appaccount;
    }

    public void setAppaccount(String appaccount) {
        this.appaccount = appaccount == null ? null : appaccount.trim();
    }

    public String getAppaccount2() {
        return appaccount2;
    }

    public void setAppaccount2(String appaccount2) {
        this.appaccount2 = appaccount2 == null ? null : appaccount2.trim();
    }

    public String getAppdesc() {
        return appdesc;
    }

    public void setAppdesc(String appdesc) {
        this.appdesc = appdesc == null ? null : appdesc.trim();
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

    public Date getMtime() {
        return mtime;
    }

    public void setMtime(Date mtime) {
        this.mtime = mtime;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid == null ? null : mid.trim();
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getSubDomainRegex() {
        return subDomainRegex;
    }

    public void setSubDomainRegex(String subDomainRegex) {
        this.subDomainRegex = subDomainRegex == null ? null : subDomainRegex.trim();
    }

    public String getAppscode() {
        return appscode;
    }

    public void setAppscode(String appscode) {
        this.appscode = appscode == null ? null : appscode.trim();
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang == null ? null : lang.trim();
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public String getContainDomain() {
        return containDomain;
    }

    public void setContainDomain(String containDomain) {
        this.containDomain = containDomain == null ? null : containDomain.trim();
    }
}