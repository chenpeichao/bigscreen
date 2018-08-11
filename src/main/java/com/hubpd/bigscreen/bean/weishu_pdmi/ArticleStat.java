package com.hubpd.bigscreen.bean.weishu_pdmi;

import java.util.Date;

public class ArticleStat {
    private Date refDate;

    private Long articleId;

    private Integer pubAccountId;

    private Date publishDate;

    private String msgid;

    private Integer msgindex;

    private Integer totalReadNum;

    private Integer totalLikeNum;

    public Date getRefDate() {
        return refDate;
    }

    public void setRefDate(Date refDate) {
        this.refDate = refDate;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Integer getPubAccountId() {
        return pubAccountId;
    }

    public void setPubAccountId(Integer pubAccountId) {
        this.pubAccountId = pubAccountId;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid == null ? null : msgid.trim();
    }

    public Integer getMsgindex() {
        return msgindex;
    }

    public void setMsgindex(Integer msgindex) {
        this.msgindex = msgindex;
    }

    public Integer getTotalReadNum() {
        return totalReadNum;
    }

    public void setTotalReadNum(Integer totalReadNum) {
        this.totalReadNum = totalReadNum;
    }

    public Integer getTotalLikeNum() {
        return totalLikeNum;
    }

    public void setTotalLikeNum(Integer totalLikeNum) {
        this.totalLikeNum = totalLikeNum;
    }
}