package com.hubpd.bigscreen.bean.weishu_pdmi;

public class ArticleWithBLOBs extends Article {

    private String content;

    private String extData;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData == null ? null : extData.trim();
    }
}