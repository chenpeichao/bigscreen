package com.hubpd.bigscreen.vo;

import java.io.Serializable;

/**
 * 运营分析接口返回临时VO
 *
 * @author cpc
 * @create 2018-09-17 20:36
 **/
public class StatisticAnalyseTmpVO implements Serializable {
    private String appName;         //应用名称
    private Long pv = 0L;           //
    private Long uv = 0L;           //
    private Long originCount;       //原创数
    private Long tracedCount;       //转载数
    private String uarAppkey;       //uar应用appkey
    private String crtMediaId;      //crt媒体id


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getPv() {
        return pv;
    }

    public void setPv(Long pv) {
        this.pv = pv;
    }

    public Long getUv() {
        return uv;
    }

    public void setUv(Long uv) {
        this.uv = uv;
    }

    public Long getOriginCount() {
        return originCount;
    }

    public void setOriginCount(Long originCount) {
        this.originCount = originCount;
    }

    public Long getTracedCount() {
        return tracedCount;
    }

    public void setTracedCount(Long tracedCount) {
        this.tracedCount = tracedCount;
    }

    public String getUarAppkey() {
        return uarAppkey;
    }

    public void setUarAppkey(String uarAppkey) {
        this.uarAppkey = uarAppkey;
    }

    public String getCrtMediaId() {
        return crtMediaId;
    }

    public void setCrtMediaId(String crtMediaId) {
        this.crtMediaId = crtMediaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatisticAnalyseTmpVO that = (StatisticAnalyseTmpVO) o;

        if (appName != null ? !appName.equals(that.appName) : that.appName != null) return false;
        if (pv != null ? !pv.equals(that.pv) : that.pv != null) return false;
        if (uv != null ? !uv.equals(that.uv) : that.uv != null) return false;
        if (originCount != null ? !originCount.equals(that.originCount) : that.originCount != null) return false;
        if (tracedCount != null ? !tracedCount.equals(that.tracedCount) : that.tracedCount != null) return false;
        if (uarAppkey != null ? !uarAppkey.equals(that.uarAppkey) : that.uarAppkey != null) return false;
        return crtMediaId != null ? crtMediaId.equals(that.crtMediaId) : that.crtMediaId == null;

    }

    @Override
    public int hashCode() {
        int result = appName != null ? appName.hashCode() : 0;
        result = 31 * result + (pv != null ? pv.hashCode() : 0);
        result = 31 * result + (uv != null ? uv.hashCode() : 0);
        result = 31 * result + (originCount != null ? originCount.hashCode() : 0);
        result = 31 * result + (tracedCount != null ? tracedCount.hashCode() : 0);
        result = 31 * result + (uarAppkey != null ? uarAppkey.hashCode() : 0);
        result = 31 * result + (crtMediaId != null ? crtMediaId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StatisticAnalyseVO{" +
                "appName='" + appName + '\'' +
                ", pv=" + pv +
                ", uv=" + uv +
                ", originCount=" + originCount +
                ", tracedCount=" + tracedCount +
                ", uarAppkey='" + uarAppkey + '\'' +
                ", crtMediaId='" + crtMediaId + '\'' +
                '}';
    }
}
