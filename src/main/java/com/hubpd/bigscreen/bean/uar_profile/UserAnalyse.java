package com.hubpd.bigscreen.bean.uar_profile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

/**
 * 用户分析
 *
 * @author cpc
 * @create 2018-08-15 16:05
 **/
@Document(indexName = "offline_user_profile_20170422_00", type = "user_tags")
public class UserAnalyse implements Serializable {
    @Id
    private String id;
    private String at;
    private Integer tag_count;
    private String age;
    private String province;
    private String country;
    private String sex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getTag_count() {
        return tag_count;
    }

    public void setTag_count(Integer tag_count) {
        this.tag_count = tag_count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAnalyse that = (UserAnalyse) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (at != null ? !at.equals(that.at) : that.at != null) return false;
        if (tag_count != null ? !tag_count.equals(that.tag_count) : that.tag_count != null) return false;
        if (age != null ? !age.equals(that.age) : that.age != null) return false;
        if (province != null ? !province.equals(that.province) : that.province != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        return sex != null ? sex.equals(that.sex) : that.sex == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (at != null ? at.hashCode() : 0);
        result = 31 * result + (tag_count != null ? tag_count.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserAnalyse{" +
                "id='" + id + '\'' +
                ", at='" + at + '\'' +
                ", tag_count=" + tag_count +
                ", age='" + age + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
