package com.hubpd.bigscreen.bean.weishu_pdmi;

import java.util.Date;

public class Article {
    private Long id;

    private Date createDate;

    private Date modifyDate;

    private Integer pubAccountId;

    private String biz;

    private String url;

    private Date publishTime;

    private String title;

    private String author;

    private String msgid;

    private Integer msgindex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getPubAccountId() {
        return pubAccountId;
    }

    public void setPubAccountId(Integer pubAccountId) {
        this.pubAccountId = pubAccountId;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz == null ? null : biz.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
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
}