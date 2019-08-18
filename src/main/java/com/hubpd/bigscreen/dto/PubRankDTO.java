package com.hubpd.bigscreen.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hubpd.bigscreen.utils.serializer.RoundDoulbeSerializer;
import com.hubpd.bigscreen.utils.serializer.ThoustandSeparatorSerializer;

/**
 * 微信公众号榜单
 *
 * @author cpc
 * @create 2018-04-16 18:05
 **/
public class PubRankDTO {
    private String wxId;                    //微信号id
    private String wxName;                  //微信名
    @JsonSerialize(using = ThoustandSeparatorSerializer.class)
    private Integer articleTotal;           //文章数
    private Integer readTotal;              //阅读总数
    private Integer likeTotal;              //点赞数
    @JsonSerialize(using = ThoustandSeparatorSerializer.class)
    private Double likeAverage = 0.0;              //平均点赞数
    @JsonSerialize(using = ThoustandSeparatorSerializer.class)
    private Double readAverage = 0.0;              //平均阅读数
    @JsonSerialize(using = ThoustandSeparatorSerializer.class)
    private Double headlineReadAverage;    //平均头条阅读数
    @JsonSerialize(using = RoundDoulbeSerializer.class)
    private Double headlineLikeAverage;    //平均头条阅读数
    @JsonSerialize(using = RoundDoulbeSerializer.class)
    private Double impactIndex = 0.0;
    @JsonSerialize(using = ThoustandSeparatorSerializer.class)
    private Integer dayType;                //统计时间类型, 7,30

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public Integer getArticleTotal() {
        return articleTotal;
    }

    public void setArticleTotal(Integer articleTotal) {
        this.articleTotal = articleTotal;
    }

    public Integer getReadTotal() {
        return readTotal;
    }

    public void setReadTotal(Integer readTotal) {
        this.readTotal = readTotal;
    }

    public Integer getLikeTotal() {
        return likeTotal;
    }

    public void setLikeTotal(Integer likeTotal) {
        this.likeTotal = likeTotal;
    }

    public Double getLikeAverage() {
        return likeAverage;
    }

    public void setLikeAverage(Double likeAverage) {
        this.likeAverage = likeAverage;
    }

    public Double getReadAverage() {
        return readAverage;
    }

    public void setReadAverage(Double readAverage) {
        this.readAverage = readAverage;
    }

    public Double getHeadlineReadAverage() {
        return headlineReadAverage;
    }

    public void setHeadlineReadAverage(Double headlineReadAverage) {
        this.headlineReadAverage = headlineReadAverage;
    }

    public Double getImpactIndex() {
        return impactIndex;
    }

    public void setImpactIndex(Double impactIndex) {
        this.impactIndex = impactIndex;
    }

    public Integer getDayType() {
        return dayType;
    }

    public void setDayType(Integer dayType) {
        this.dayType = dayType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PubRankDTO that = (PubRankDTO) o;

        if (wxId != null ? !wxId.equals(that.wxId) : that.wxId != null) return false;
        if (wxName != null ? !wxName.equals(that.wxName) : that.wxName != null) return false;
        if (articleTotal != null ? !articleTotal.equals(that.articleTotal) : that.articleTotal != null) return false;
        if (readTotal != null ? !readTotal.equals(that.readTotal) : that.readTotal != null) return false;
        if (likeTotal != null ? !likeTotal.equals(that.likeTotal) : that.likeTotal != null) return false;
        if (likeAverage != null ? !likeAverage.equals(that.likeAverage) : that.likeAverage != null) return false;
        if (readAverage != null ? !readAverage.equals(that.readAverage) : that.readAverage != null) return false;
        if (headlineReadAverage != null ? !headlineReadAverage.equals(that.headlineReadAverage) : that.headlineReadAverage != null)
            return false;
        if (impactIndex != null ? !impactIndex.equals(that.impactIndex) : that.impactIndex != null) return false;
        return dayType != null ? dayType.equals(that.dayType) : that.dayType == null;

    }

    @Override
    public int hashCode() {
        int result = wxId != null ? wxId.hashCode() : 0;
        result = 31 * result + (wxName != null ? wxName.hashCode() : 0);
        result = 31 * result + (articleTotal != null ? articleTotal.hashCode() : 0);
        result = 31 * result + (readTotal != null ? readTotal.hashCode() : 0);
        result = 31 * result + (likeTotal != null ? likeTotal.hashCode() : 0);
        result = 31 * result + (likeAverage != null ? likeAverage.hashCode() : 0);
        result = 31 * result + (readAverage != null ? readAverage.hashCode() : 0);
        result = 31 * result + (headlineReadAverage != null ? headlineReadAverage.hashCode() : 0);
        result = 31 * result + (impactIndex != null ? impactIndex.hashCode() : 0);
        result = 31 * result + (dayType != null ? dayType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PubRankDTO{" +
                "wxId='" + wxId + '\'' +
                ", wxName='" + wxName + '\'' +
                ", articleTotal=" + articleTotal +
                ", readTotal=" + readTotal +
                ", likeTotal=" + likeTotal +
                ", likeAverage=" + likeAverage +
                ", readAverage=" + readAverage +
                ", headlineReadAverage=" + headlineReadAverage +
                ", impactIndex=" + impactIndex +
                ", dayType=" + dayType +
                '}';
    }
}
