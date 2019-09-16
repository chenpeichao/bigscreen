package com.hubpd.bigscreen.dto;

/**
 * 网站的pv、uv统计数据DTO
 *
 * @author ceek
 * @create 2019-09-16 10:31
 **/
public class WebAtHourDTO {
    private Integer maxHour;
    private Long pv;
    private Long uv;

    public Integer getMaxHour() {
        return maxHour;
    }

    public void setMaxHour(Integer maxHour) {
        this.maxHour = maxHour;
    }

    public Long getPv() {
        return pv;
    }

    public void setPv(Long pv) {
        this.pv = pv;
    }

    public Long getUv() {
        return uv;
    }

    public void setUv(Long uv) {
        this.uv = uv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebAtHourDTO that = (WebAtHourDTO) o;

        if (maxHour != null ? !maxHour.equals(that.maxHour) : that.maxHour != null) return false;
        if (pv != null ? !pv.equals(that.pv) : that.pv != null) return false;
        return uv != null ? uv.equals(that.uv) : that.uv == null;

    }

    @Override
    public int hashCode() {
        int result = maxHour != null ? maxHour.hashCode() : 0;
        result = 31 * result + (pv != null ? pv.hashCode() : 0);
        result = 31 * result + (uv != null ? uv.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WebAtHourDTO{" +
                "maxHour=" + maxHour +
                ", pv=" + pv +
                ", uv=" + uv +
                '}';
    }
}
