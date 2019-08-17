package com.hubpd.bigscreen.bean.weishu_pdmi;

public class PubRank {
    private Integer catalogId;

    private Integer pubAccountId;

    private Integer articleTotal;

    private Integer messageTotal;

    private Integer readTotal;

    private Integer likeTotal;

    private Float readAverage;

    private Float likeAverage;

    private Float headlineReadAverage;

    private Float headlineLikeAverage;

    private Float impactIndex;

    private Integer dayType;

    public Integer getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Integer catalogId) {
        this.catalogId = catalogId;
    }

    public Integer getPubAccountId() {
        return pubAccountId;
    }

    public void setPubAccountId(Integer pubAccountId) {
        this.pubAccountId = pubAccountId;
    }

    public Integer getArticleTotal() {
        return articleTotal;
    }

    public void setArticleTotal(Integer articleTotal) {
        this.articleTotal = articleTotal;
    }

    public Integer getMessageTotal() {
        return messageTotal;
    }

    public void setMessageTotal(Integer messageTotal) {
        this.messageTotal = messageTotal;
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

    public Float getReadAverage() {
        return readAverage;
    }

    public void setReadAverage(Float readAverage) {
        this.readAverage = readAverage;
    }

    public Float getLikeAverage() {
        return likeAverage;
    }

    public void setLikeAverage(Float likeAverage) {
        this.likeAverage = likeAverage;
    }

    public Float getHeadlineReadAverage() {
        return headlineReadAverage;
    }

    public void setHeadlineReadAverage(Float headlineReadAverage) {
        this.headlineReadAverage = headlineReadAverage;
    }

    public Float getHeadlineLikeAverage() {
        return headlineLikeAverage;
    }

    public void setHeadlineLikeAverage(Float headlineLikeAverage) {
        this.headlineLikeAverage = headlineLikeAverage;
    }

    public Float getImpactIndex() {
        return impactIndex;
    }

    public void setImpactIndex(Float impactIndex) {
        this.impactIndex = impactIndex;
    }

    public Integer getDayType() {
        return dayType;
    }

    public void setDayType(Integer dayType) {
        this.dayType = dayType;
    }
}