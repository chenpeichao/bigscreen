package com.hubpd.bigscreen.vo;

/**
 * 微信运营接口vo
 * Created by ceek on 2018-08-09 22:53.
 */
public class WXUserAnalyseVO {
    private String wxNo;                        //微信公众号
    private String wxName;                        //微信名称
    private String dateTime;                    //数据对应日期(为请求参数中时间的前七天中某一天，格式为yyyy-MM-dd)
    private Integer newFollowNum = 0;              //新增关注人数
    private Integer cumulateFollowNum = 0;         //累计关注人数
    private Integer cancelFollowNum = 0;            //取消关注人数
    private Integer realIncreaseFollowNum = 0;      //净增关注人数
    private Long likeNum = 0l;                       //点赞总数
    private Long readNum = 0l;                       //阅读总数

    public String getWxNo() {
        return wxNo;
    }

    public void setWxNo(String wxNo) {
        this.wxNo = wxNo;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getNewFollowNum() {
        return newFollowNum;
    }

    public void setNewFollowNum(Integer newFollowNum) {
        this.newFollowNum = newFollowNum;
    }

    public Integer getCumulateFollowNum() {
        return cumulateFollowNum;
    }

    public void setCumulateFollowNum(Integer cumulateFollowNum) {
        this.cumulateFollowNum = cumulateFollowNum;
    }

    public Integer getCancelFollowNum() {
        return cancelFollowNum;
    }

    public void setCancelFollowNum(Integer cancelFollowNum) {
        this.cancelFollowNum = cancelFollowNum;
    }

    public Integer getRealIncreaseFollowNum() {
        return realIncreaseFollowNum;
    }

    public void setRealIncreaseFollowNum(Integer realIncreaseFollowNum) {
        this.realIncreaseFollowNum = realIncreaseFollowNum;
    }

    public Long getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Long likeNum) {
        this.likeNum = likeNum;
    }

    public Long getReadNum() {
        return readNum;
    }

    public void setReadNum(Long readNum) {
        this.readNum = readNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WXUserAnalyseVO that = (WXUserAnalyseVO) o;

        if (wxNo != null ? !wxNo.equals(that.wxNo) : that.wxNo != null) return false;
        if (wxName != null ? !wxName.equals(that.wxName) : that.wxName != null) return false;
        if (dateTime != null ? !dateTime.equals(that.dateTime) : that.dateTime != null) return false;
        if (newFollowNum != null ? !newFollowNum.equals(that.newFollowNum) : that.newFollowNum != null) return false;
        if (cumulateFollowNum != null ? !cumulateFollowNum.equals(that.cumulateFollowNum) : that.cumulateFollowNum != null)
            return false;
        if (cancelFollowNum != null ? !cancelFollowNum.equals(that.cancelFollowNum) : that.cancelFollowNum != null)
            return false;
        if (realIncreaseFollowNum != null ? !realIncreaseFollowNum.equals(that.realIncreaseFollowNum) : that.realIncreaseFollowNum != null)
            return false;
        if (likeNum != null ? !likeNum.equals(that.likeNum) : that.likeNum != null) return false;
        return readNum != null ? readNum.equals(that.readNum) : that.readNum == null;

    }

    @Override
    public int hashCode() {
        int result = wxNo != null ? wxNo.hashCode() : 0;
        result = 31 * result + (wxName != null ? wxName.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (newFollowNum != null ? newFollowNum.hashCode() : 0);
        result = 31 * result + (cumulateFollowNum != null ? cumulateFollowNum.hashCode() : 0);
        result = 31 * result + (cancelFollowNum != null ? cancelFollowNum.hashCode() : 0);
        result = 31 * result + (realIncreaseFollowNum != null ? realIncreaseFollowNum.hashCode() : 0);
        result = 31 * result + (likeNum != null ? likeNum.hashCode() : 0);
        result = 31 * result + (readNum != null ? readNum.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WXUserAnalyseVO{" +
                "wxNo='" + wxNo + '\'' +
                ", wxName='" + wxName + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", newFollowNum=" + newFollowNum +
                ", cumulateFollowNum=" + cumulateFollowNum +
                ", cancelFollowNum=" + cancelFollowNum +
                ", realIncreaseFollowNum=" + realIncreaseFollowNum +
                ", likeNum=" + likeNum +
                ", readNum=" + readNum +
                '}';
    }
}
