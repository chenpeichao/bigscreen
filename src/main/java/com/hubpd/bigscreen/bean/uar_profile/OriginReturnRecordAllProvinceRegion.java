package com.hubpd.bigscreen.bean.uar_profile;

public class OriginReturnRecordAllProvinceRegion {
    private Integer id;

    private String originId;

    private String returnDate;

    private String returnJson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId == null ? null : originId.trim();
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate == null ? null : returnDate.trim();
    }

    public String getReturnJson() {
        return returnJson;
    }

    public void setReturnJson(String returnJson) {
        this.returnJson = returnJson == null ? null : returnJson.trim();
    }
}