package com.hubpd.bigscreen.bean.uar_profile;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * 内容库文章
 *
 * @author cpc
 * @create 2018-08-13 16:08
 **/
@Document(indexName="caiyun",type="spider_result")
public class Article implements Serializable {
    @Id
    private String id;
    private Integer taskid;  //标题
    private String mediaName;  //摘要
    private String source;  //内容
    private String title;  //发表时间
    private String url;  //点击率
    private String mediaType;	//作者

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (id != null ? !id.equals(article.id) : article.id != null) return false;
        if (taskid != null ? !taskid.equals(article.taskid) : article.taskid != null) return false;
        if (mediaName != null ? !mediaName.equals(article.mediaName) : article.mediaName != null) return false;
        if (source != null ? !source.equals(article.source) : article.source != null) return false;
        if (title != null ? !title.equals(article.title) : article.title != null) return false;
        if (url != null ? !url.equals(article.url) : article.url != null) return false;
        return mediaType != null ? mediaType.equals(article.mediaType) : article.mediaType == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (taskid != null ? taskid.hashCode() : 0);
        result = 31 * result + (mediaName != null ? mediaName.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (mediaType != null ? mediaType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", taskid=" + taskid +
                ", mediaName='" + mediaName + '\'' +
                ", source='" + source + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }
}
