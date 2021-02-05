package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Manufacture {

    @SerializedName("manufactureId")
    @Expose
    private String manufactureId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("sortOrder")
    @Expose
    private Object sortOrder;
    @SerializedName("categoryDisplay")
    @Expose
    private Object categoryDisplay;
    @SerializedName("manufactureDescriptions")
    @Expose
    private List<ManufactureDescription> manufactureDescriptions = null;

    public String getManufactureId() {
        return manufactureId;
    }

    public void setManufactureId(String manufactureId) {
        this.manufactureId = manufactureId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ManufactureDescription> getManufactureDescriptions() {
        return manufactureDescriptions;
    }

    public void setManufactureDescriptions(List<ManufactureDescription> manufactureDescriptions) {
        this.manufactureDescriptions = manufactureDescriptions;
    }

    public Object getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Object sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Object getCategoryDisplay() {
        return categoryDisplay;
    }

    public void setCategoryDisplay(Object categoryDisplay) {
        this.categoryDisplay = categoryDisplay;
    }
}