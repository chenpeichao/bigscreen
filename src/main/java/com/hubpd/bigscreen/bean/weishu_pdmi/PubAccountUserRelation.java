package com.hubpd.bigscreen.bean.weishu_pdmi;

import java.util.Date;

public class PubAccountUserRelation {
    private Integer pubAccountId;

    private String userId;

    private Integer userFollow;

    private Date createTime;

    private Date expiredTime;

    private String orgId;

    public Integer getPubAccountId() {
        return pubAccountId;
    }

    public void setPubAccountId(Integer pubAccountId) {
        this.pubAccountId = pubAccountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getUserFollow() {
        return userFollow;
    }

    public void setUserFollow(Integer userFollow) {
        this.userFollow = userFollow;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }
}