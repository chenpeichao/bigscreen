package com.hubpd.bigscreen.dto;

/**
 * 客户端的昨日及以前的新增用户及活跃用户数
 *
 * @author ceek
 * @create 2019-09-16 14:48
 **/
public class AppAtDayDTO {
    private Long activeUV;  //活跃用户
    private Long newUV;     //新增用户

    public Long getActiveUV() {
        return activeUV;
    }

    public void setActiveUV(Long activeUV) {
        this.activeUV = activeUV;
    }

    public Long getNewUV() {
        return newUV;
    }

    public void setNewUV(Long newUV) {
        this.newUV = newUV;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppAtDayDTO that = (AppAtDayDTO) o;

        if (activeUV != null ? !activeUV.equals(that.activeUV) : that.activeUV != null) return false;
        return newUV != null ? newUV.equals(that.newUV) : that.newUV == null;

    }

    @Override
    public int hashCode() {
        int result = activeUV != null ? activeUV.hashCode() : 0;
        result = 31 * result + (newUV != null ? newUV.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AppAtDayDTO{" +
                "activeUV=" + activeUV +
                ", newUV=" + newUV +
                '}';
    }
}
