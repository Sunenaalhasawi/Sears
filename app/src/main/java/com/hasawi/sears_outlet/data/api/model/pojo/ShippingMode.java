package com.hasawi.sears_outlet.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingMode {
    @SerializedName("shippingId")
    @Expose
    private String shippingId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("charge")
    @Expose
    private Double charge;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("comment")
    @Expose
    private Object comment;
    @SerializedName("sortOrder")
    @Expose
    private String sortOrder;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("targetAmount")
    @Expose
    private Double targetAmount;

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
        this.comment = comment;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

}
