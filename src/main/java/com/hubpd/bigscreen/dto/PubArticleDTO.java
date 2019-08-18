package com.hubpd.bigscreen.dto;

/**
 * 公众号文章
 *
 * @author ceek
 * @create 2019-08-18 15:23
 **/
public class PubArticleDTO {
    private String articleTitle;
    private String articleUrl;
    private String publishTime;
    private Integer readTotal;              //阅读总数
    private Integer likeTotal;              //点赞总数
    private Integer articleMsgIndex;        //文章在群发消息中的位置(1:头条；非1:非头条)

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

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
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

    public Integer getArticleMsgIndex() {
        return articleMsgIndex;
    }

    public void setArticleMsgIndex(Integer articleMsgIndex) {
        this.articleMsgIndex = articleMsgIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PubArticleDTO that = (PubArticleDTO) o;

        if (articleTitle != null ? !articleTitle.equals(that.articleTitle) : that.articleTitle != null) return false;
        if (articleUrl != null ? !articleUrl.equals(that.articleUrl) : that.articleUrl != null) return false;
        if (publishTime != null ? !publishTime.equals(that.publishTime) : that.publishTime != null) return false;
        if (readTotal != null ? !readTotal.equals(that.readTotal) : that.readTotal != null) return false;
        if (likeTotal != null ? !likeTotal.equals(that.likeTotal) : that.likeTotal != null) return false;
        return articleMsgIndex != null ? articleMsgIndex.equals(that.articleMsgIndex) : that.articleMsgIndex == null;

    }

    @Override
    public int hashCode() {
        int result = articleTitle != null ? articleTitle.hashCode() : 0;
        result = 31 * result + (articleUrl != null ? articleUrl.hashCode() : 0);
        result = 31 * result + (publishTime != null ? publishTime.hashCode() : 0);
        result = 31 * result + (readTotal != null ? readTotal.hashCode() : 0);
        result = 31 * result + (likeTotal != null ? likeTotal.hashCode() : 0);
        result = 31 * result + (articleMsgIndex != null ? articleMsgIndex.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PubArticleDTO{" +
                "articleTitle='" + articleTitle + '\'' +
                ", articleUrl='" + articleUrl + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", readTotal=" + readTotal +
                ", likeTotal=" + likeTotal +
                ", articleMsgIndex=" + articleMsgIndex +
                '}';
    }
}
