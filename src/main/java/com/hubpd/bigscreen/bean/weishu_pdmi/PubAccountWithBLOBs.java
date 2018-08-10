package com.hubpd.bigscreen.bean.weishu_pdmi;

public class PubAccountWithBLOBs extends PubAccount {
    private String intro;

    private String certinfo;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro == null ? null : intro.trim();
    }

    public String getCertinfo() {
        return certinfo;
    }

    public void setCertinfo(String certinfo) {
        this.certinfo = certinfo == null ? null : certinfo.trim();
    }
}