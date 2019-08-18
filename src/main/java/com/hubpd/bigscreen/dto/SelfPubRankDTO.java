package com.hubpd.bigscreen.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hubpd.bigscreen.utils.serializer.ThoustandSeparatorSerializer;

/**
 * 自有微信公众号榜单
 *
 * @author ceek
 * @create 2019-08-18 13:59
 **/
public class SelfPubRankDTO {
    private String wxId;                    //微信号id
    private String wxName;                 //微信名
    @JsonSerialize(using = ThoustandSeparatorSerializer.class)
    private Long articleReadNum;           //阅读数
    @JsonSerialize(using = ThoustandSeparatorSerializer.class)
    private Long articleLikeNum;           //点赞数
    @JsonSerialize(using = ThoustandSeparatorSerializer.class)
    private Long newSum;                   //新增关注人数
    @JsonSerialize(using = ThoustandSeparatorSerializer.class)
    private Long cancelSum;              //取消关注人数
    @JsonSerialize(using = ThoustandSeparatorSerializer.class)
    private Long cumulateUser;              //累计关注人数
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

    public Long getArticleReadNum() {
        return articleReadNum;
    }

    public void setArticleReadNum(Long articleReadNum) {
        this.articleReadNum = articleReadNum;
    }

    public Long getArticleLikeNum() {
        return articleLikeNum;
    }

    public void setArticleLikeNum(Long articleLikeNum) {
        this.articleLikeNum = articleLikeNum;
    }

    public Long getNewSum() {
        return newSum;
    }

    public void setNewSum(Long newSum) {
        this.newSum = newSum;
    }

    public Long getCancelSum() {
        return cancelSum;
    }

    public void setCancelSum(Long cancelSum) {
        this.cancelSum = cancelSum;
    }

    public Long getCumulateUser() {
        return cumulateUser;
    }

    public void setCumulateUser(Long cumulateUser) {
        this.cumulateUser = cumulateUser;
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

        SelfPubRankDTO that = (SelfPubRankDTO) o;

        if (wxId != null ? !wxId.equals(that.wxId) : that.wxId != null) return false;
        if (wxName != null ? !wxName.equals(that.wxName) : that.wxName != null) return false;
        if (articleReadNum != null ? !articleReadNum.equals(that.articleReadNum) : that.articleReadNum != null)
            return false;
        if (articleLikeNum != null ? !articleLikeNum.equals(that.articleLikeNum) : that.articleLikeNum != null)
            return false;
        if (newSum != null ? !newSum.equals(that.newSum) : that.newSum != null) return false;
        if (cancelSum != null ? !cancelSum.equals(that.cancelSum) : that.cancelSum != null) return false;
        if (cumulateUser != null ? !cumulateUser.equals(that.cumulateUser) : that.cumulateUser != null) return false;
        return dayType != null ? dayType.equals(that.dayType) : that.dayType == null;

    }

    @Override
    public int hashCode() {
        int result = wxId != null ? wxId.hashCode() : 0;
        result = 31 * result + (wxName != null ? wxName.hashCode() : 0);
        result = 31 * result + (articleReadNum != null ? articleReadNum.hashCode() : 0);
        result = 31 * result + (articleLikeNum != null ? articleLikeNum.hashCode() : 0);
        result = 31 * result + (newSum != null ? newSum.hashCode() : 0);
        result = 31 * result + (cancelSum != null ? cancelSum.hashCode() : 0);
        result = 31 * result + (cumulateUser != null ? cumulateUser.hashCode() : 0);
        result = 31 * result + (dayType != null ? dayType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SelfPubRankDTO{" +
                "wxId='" + wxId + '\'' +
                ", wxName='" + wxName + '\'' +
                ", articleReadNum=" + articleReadNum +
                ", articleLikeNum=" + articleLikeNum +
                ", newSum=" + newSum +
                ", cancelSum=" + cancelSum +
                ", cumulateUser=" + cumulateUser +
                ", dayType=" + dayType +
                '}';
    }
}
