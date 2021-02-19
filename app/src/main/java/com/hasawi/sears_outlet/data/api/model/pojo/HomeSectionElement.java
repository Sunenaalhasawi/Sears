package com.hasawi.sears_outlet.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeSectionElement {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("callBack")
    @Expose
    private String callBack;
    @SerializedName("attributeIds")
    @Expose
    private String attributeIds;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCallBack() {
        return callBack;
    }

    public void setCallBack(String callBack) {
        this.callBack = callBack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttributeIds() {
        return attributeIds;
    }

    public void setAttributeIds(String attributeIds) {
        this.attributeIds = attributeIds;
    }
}
