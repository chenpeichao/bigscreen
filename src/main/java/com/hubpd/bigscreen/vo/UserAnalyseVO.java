package com.hubpd.bigscreen.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户分析VO
 *
 * @author cpc
 * @create 2018-08-16 18:42
 **/
public class UserAnalyseVO implements Serializable {
    private Map<String, Long> gender = new HashMap<String, Long>();
    private String appName;
    private List<UserAnalyseRegionVO> region = new ArrayList<UserAnalyseRegionVO>();
    private Map<String, Long> age = new HashMap<String, Long>();

    public Map<String, Long> getGender() {
        return gender;
    }

    public void setGender(Map<String, Long> gender) {
        this.gender = gender;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<UserAnalyseRegionVO> getRegion() {
        return region;
    }

    public void setRegion(List<UserAnalyseRegionVO> region) {
        this.region = region;
    }

    public Map<String, Long> getAge() {
        return age;
    }

    public void setAge(Map<String, Long> age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAnalyseVO that = (UserAnalyseVO) o;

        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (appName != null ? !appName.equals(that.appName) : that.appName != null) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;
        return age != null ? age.equals(that.age) : that.age == null;

    }

    @Override
    public int hashCode() {
        int result = gender != null ? gender.hashCode() : 0;
        result = 31 * result + (appName != null ? appName.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserAnalyseVO{" +
                "gender=" + gender +
                ", appName='" + appName + '\'' +
                ", region=" + region +
                ", age=" + age +
                '}';
    }
}
