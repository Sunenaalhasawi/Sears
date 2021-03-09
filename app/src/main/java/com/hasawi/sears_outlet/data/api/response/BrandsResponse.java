package com.hasawi.sears_outlet.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hasawi.sears_outlet.data.api.model.pojo.Manufacture;

import java.util.List;

public class BrandsResponse {
    @SerializedName("data")
    @Expose
    List<Manufacture> manufactureList;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Manufacture> getManufactureList() {
        return manufactureList;
    }

    public void setManufactureList(List<Manufacture> manufactureList) {
        this.manufactureList = manufactureList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
