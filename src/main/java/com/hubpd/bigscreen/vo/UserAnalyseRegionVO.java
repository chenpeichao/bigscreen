package com.hubpd.bigscreen.vo;

import java.io.Serializable;

/**
 * 用户分析地域vo
 *
 * @author cpc
 * @create 2018-08-15 15:11
 **/
public class UserAnalyseRegionVO implements Serializable {
    private String regionName;
    private Long num;

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAnalyseRegionVO that = (UserAnalyseRegionVO) o;

        if (regionName != null ? !regionName.equals(that.regionName) : that.regionName != null) return false;
        return num != null ? num.equals(that.num) : that.num == null;

    }

    @Override
    public int hashCode() {
        int result = regionName != null ? regionName.hashCode() : 0;
        result = 31 * result + (num != null ? num.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserAnalyseRegionVO{" +
                "regionName='" + regionName + '\'' +
                ", num=" + num +
                '}';
    }
}
