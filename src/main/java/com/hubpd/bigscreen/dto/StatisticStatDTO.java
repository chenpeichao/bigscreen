package com.hubpd.bigscreen.dto;

/**
 * 统计指标DTO
 *
 * @author ceek
 * @create 2019-08-19 9:50
 **/
public class StatisticStatDTO {
    private Long pv;
    private Long uv;

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

        StatisticStatDTO that = (StatisticStatDTO) o;

        if (pv != null ? !pv.equals(that.pv) : that.pv != null) return false;
        return uv != null ? uv.equals(that.uv) : that.uv == null;

    }

    @Override
    public int hashCode() {
        int result = pv != null ? pv.hashCode() : 0;
        result = 31 * result + (uv != null ? uv.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StatisticStatDTO{" +
                "pv=" + pv +
                ", uv=" + uv +
                '}';
    }
}
