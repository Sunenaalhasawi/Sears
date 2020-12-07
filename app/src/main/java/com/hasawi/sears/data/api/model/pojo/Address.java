package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("addressId")
    @Expose
    private String addressId;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("flat")
    @Expose
    private String flat;
    @SerializedName("block")
    @Expose
    private String block;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("country")
    @Expose
    private String country;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}

