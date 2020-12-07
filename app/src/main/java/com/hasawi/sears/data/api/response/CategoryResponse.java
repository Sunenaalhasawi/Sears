
package com.hasawi.sears.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hasawi.sears.data.api.model.pojo.Category;

import java.util.List;

public class CategoryResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Category> data = null;

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

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

}
