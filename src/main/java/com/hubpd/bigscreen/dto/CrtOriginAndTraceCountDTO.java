package com.hubpd.bigscreen.dto;

import java.io.Serializable;

/**
 * crt接口返回的原创和转载数
 *
 * @author cpc
 * @create 2018-09-14 10:44
 **/
public class CrtOriginAndTraceCountDTO implements Serializable {
    private String name;        //crt端应用名称
    private String MediaID;     //crt端媒体id
    private Long originCount;   //原创数
    private Long tracedCount;   //转载数

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMediaID() {
        return MediaID;
    }

    public void setMediaID(String mediaID) {
        MediaID = mediaID;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CrtOriginAndTraceCountDTO that = (CrtOriginAndTraceCountDTO) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (MediaID != null ? !MediaID.equals(that.MediaID) : that.MediaID != null) return false;
        if (originCount != null ? !originCount.equals(that.originCount) : that.originCount != null) return false;
        return tracedCount != null ? tracedCount.equals(that.tracedCount) : that.tracedCount == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (MediaID != null ? MediaID.hashCode() : 0);
        result = 31 * result + (originCount != null ? originCount.hashCode() : 0);
        result = 31 * result + (tracedCount != null ? tracedCount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CrtOriginAndTraceCountDTO{" +
                "name='" + name + '\'' +
                ", MediaID='" + MediaID + '\'' +
                ", originCount=" + originCount +
                ", tracedCount=" + tracedCount +
                '}';
    }
}
