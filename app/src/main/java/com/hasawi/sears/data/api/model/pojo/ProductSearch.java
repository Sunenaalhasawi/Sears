package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductSearch {

    @SerializedName("objectID")
    @Expose
    private String objectID;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("nameEn")
    @Expose
    private String nameEn;
    @SerializedName("nameAr")
    @Expose
    private String nameAr;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
