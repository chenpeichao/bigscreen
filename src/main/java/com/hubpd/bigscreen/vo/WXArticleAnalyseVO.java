package com.hubpd.bigscreen.vo;

/**
 * 微信内容运营接口VO
 *
 * @author cpc
 * @create 2018-08-11 17:58
 **/
public class WXArticleAnalyseVO {
    private Integer articleId;      //内容id
    private String articleTitle;    //文章标题
    private String articleUrl;      //文章链接(固定链接)
    private String dateTime;       //时间
    private Integer readNum;        //阅读数
    private Integer likeNum;        //点赞数
    private String wxNo;            //微信号
    private String wxName;          //微信名称

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WXArticleAnalyseVO that = (WXArticleAnalyseVO) o;

        if (articleId != null ? !articleId.equals(that.articleId) : that.articleId != null) return false;
        if (articleTitle != null ? !articleTitle.equals(that.articleTitle) : that.articleTitle != null) return false;
        if (articleUrl != null ? !articleUrl.equals(that.articleUrl) : that.articleUrl != null) return false;
        if (dateTime != null ? !dateTime.equals(that.dateTime) : that.dateTime != null) return false;
        if (readNum != null ? !readNum.equals(that.readNum) : that.readNum != null) return false;
        if (likeNum != null ? !likeNum.equals(that.likeNum) : that.likeNum != null) return false;
        if (wxNo != null ? !wxNo.equals(that.wxNo) : that.wxNo != null) return false;
        return wxName != null ? wxName.equals(that.wxName) : that.wxName == null;

    }

    @Override
    public int hashCode() {
        int result = articleId != null ? articleId.hashCode() : 0;
        result = 31 * result + (articleTitle != null ? articleTitle.hashCode() : 0);
        result = 31 * result + (articleUrl != null ? articleUrl.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (readNum != null ? readNum.hashCode() : 0);
        result = 31 * result + (likeNum != null ? likeNum.hashCode() : 0);
        result = 31 * result + (wxNo != null ? wxNo.hashCode() : 0);
        result = 31 * result + (wxName != null ? wxName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WXArticleAnalyseVO{" +
                "articleId=" + articleId +
                ", articleTitle='" + articleTitle + '\'' +
                ", articleUrl='" + articleUrl + '\'' +
                ", dateTime=" + dateTime +
                ", readNum=" + readNum +
                ", likeNum=" + likeNum +
                ", wxNo='" + wxNo + '\'' +
                ", wxName='" + wxName + '\'' +
                '}';
    }
}
