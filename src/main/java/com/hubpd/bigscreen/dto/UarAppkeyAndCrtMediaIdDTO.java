package com.hubpd.bigscreen.dto;

import java.io.Serializable;

/**
 * uar
 *
 * @author cpc
 * @create 2018-09-17 10:31
 **/
public class UarAppkeyAndCrtMediaIdDTO implements Serializable {
    private Integer id;         //id
    private String uarMedia;    //uar的appkey
    private String crtMedia;    //crt的mediaId
    private String uar;         //uar的机构id
    private String crt;         //crt的机构id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUarMedia() {
        return uarMedia;
    }

    public void setUarMedia(String uarMedia) {
        this.uarMedia = uarMedia;
    }

    public String getCrtMedia() {
        return crtMedia;
    }

    public void setCrtMedia(String crtMedia) {
        this.crtMedia = crtMedia;
    }

    public String getUar() {
        return uar;
    }

    public void setUar(String uar) {
        this.uar = uar;
    }

    public String getCrt() {
        return crt;
    }

    public void setCrt(String crt) {
        this.crt = crt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UarAppkeyAndCrtMediaIdDTO that = (UarAppkeyAndCrtMediaIdDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (uarMedia != null ? !uarMedia.equals(that.uarMedia) : that.uarMedia != null) return false;
        if (crtMedia != null ? !crtMedia.equals(that.crtMedia) : that.crtMedia != null) return false;
        if (uar != null ? !uar.equals(that.uar) : that.uar != null) return false;
        return crt != null ? crt.equals(that.crt) : that.crt == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uarMedia != null ? uarMedia.hashCode() : 0);
        result = 31 * result + (crtMedia != null ? crtMedia.hashCode() : 0);
        result = 31 * result + (uar != null ? uar.hashCode() : 0);
        result = 31 * result + (crt != null ? crt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UarAppkeyAndCrtMediaIdDTO{" +
                "id=" + id +
                ", uarMedia='" + uarMedia + '\'' +
                ", crtMedia='" + crtMedia + '\'' +
                ", uar='" + uar + '\'' +
                ", crt='" + crt + '\'' +
                '}';
    }
}
