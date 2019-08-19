package com.hubpd.bigscreen.bean.uar_basic;

public class Media {
    private String mediaid;

    private String medianame;

    private Boolean status;

    private Boolean sysType;

    public String getMediaid() {
        return mediaid;
    }

    public void setMediaid(String mediaid) {
        this.mediaid = mediaid == null ? null : mediaid.trim();
    }

    public String getMedianame() {
        return medianame;
    }

    public void setMedianame(String medianame) {
        this.medianame = medianame == null ? null : medianame.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getSysType() {
        return sysType;
    }

    public void setSysType(Boolean sysType) {
        this.sysType = sysType;
    }
}