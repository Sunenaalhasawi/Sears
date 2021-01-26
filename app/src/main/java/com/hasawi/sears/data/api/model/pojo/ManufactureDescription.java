package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ManufactureDescription {

    @SerializedName("manufactureDescriptionId")
    @Expose
    private String manufactureDescriptionId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private Object slug;
    @SerializedName("brandImageUrl")
    @Expose
    private Object brandImageUrl;
    @SerializedName("language")
    @Expose
    private Language language;

    public String getManufactureDescriptionId() {
        return manufactureDescriptionId;
    }

    public void setManufactureDescriptionId(String manufactureDescriptionId) {
        this.manufactureDescriptionId = manufactureDescriptionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getSlug() {
        return slug;
    }

    public void setSlug(Object slug) {
        this.slug = slug;
    }

    public Object getBrandImageUrl() {
        return brandImageUrl;
    }

    public void setBrandImageUrl(Object brandImageUrl) {
        this.brandImageUrl = brandImageUrl;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

}
