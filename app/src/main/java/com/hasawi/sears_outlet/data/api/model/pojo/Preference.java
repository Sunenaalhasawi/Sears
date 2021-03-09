package com.hasawi.sears_outlet.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Preference {
    @SerializedName("preferenceId")
    @Expose
    private String preferenceId;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("brandId")
    @Expose
    private Object brandId;

    public String getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(String preferenceId) {
        this.preferenceId = preferenceId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Object getBrandId() {
        return brandId;
    }

    public void setBrandId(Object brandId) {
        this.brandId = brandId;
    }
}