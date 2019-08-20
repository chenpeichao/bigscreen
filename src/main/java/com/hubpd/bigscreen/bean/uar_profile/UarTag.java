package com.hubpd.bigscreen.bean.uar_profile;

import java.util.Date;

public class UarTag {
    private Integer tagId;

    private String tag;

    private String tagTw;

    private String tagEn;

    private Integer parentTagId;

    private Integer tagType;

    private Integer ownerAppId;

    private String describe;

    private Date lastModify;

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public String getTagTw() {
        return tagTw;
    }

    public void setTagTw(String tagTw) {
        this.tagTw = tagTw == null ? null : tagTw.trim();
    }

    public String getTagEn() {
        return tagEn;
    }

    public void setTagEn(String tagEn) {
        this.tagEn = tagEn == null ? null : tagEn.trim();
    }

    public Integer getParentTagId() {
        return parentTagId;
    }

    public void setParentTagId(Integer parentTagId) {
        this.parentTagId = parentTagId;
    }

    public Integer getTagType() {
        return tagType;
    }

    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }

    public Integer getOwnerAppId() {
        return ownerAppId;
    }

    public void setOwnerAppId(Integer ownerAppId) {
        this.ownerAppId = ownerAppId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe == null ? null : describe.trim();
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }
}