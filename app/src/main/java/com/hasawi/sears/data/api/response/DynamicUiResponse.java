package com.hasawi.sears.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hasawi.sears.data.api.model.pojo.Banner;
import com.hasawi.sears.data.api.model.pojo.Section;

import java.util.HashMap;
import java.util.List;

public class DynamicUiResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private HashMap<String, UiData> data;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

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

    public HashMap<String, UiData> getData() {
        return data;
    }

    public void setData(HashMap<String, UiData> data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public class UiData {

        @SerializedName("banner")
        @Expose
        private List<Banner> bannerList;
        @SerializedName("section")
        @Expose
        private List<Section> section = null;
        @SerializedName("categoryName")
        @Expose
        String categoryName;
        @SerializedName("categoryId")
        @Expose
        String categoryId;


        public List<Section> getSection() {
            return section;
        }

        public void setSection(List<Section> section) {
            this.section = section;
        }

        public List<Banner> getBannerList() {
            return bannerList;
        }

        public void setBannerList(List<Banner> bannerList) {
            this.bannerList = bannerList;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
    }


}

