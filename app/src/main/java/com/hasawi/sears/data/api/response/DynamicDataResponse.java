package com.hasawi.sears.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hasawi.sears.data.api.model.pojo.Category;

import java.util.List;

public class DynamicDataResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("statusCode")
    @Expose
    private int statusCode;

    /**
     * No args constructor for use in serialization
     */
    public DynamicDataResponse() {
    }

    /**
     * @param data
     * @param message
     * @param status
     * @param statusCode
     */
    public DynamicDataResponse(String status, String message, Data data, int statusCode) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DynamicDataResponse withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DynamicDataResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public DynamicDataResponse withData(Data data) {
        this.data = data;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public DynamicDataResponse withStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }


    public static class Data {

        @SerializedName("gender")
        @Expose
        private List<String> gender = null;
        @SerializedName("sizes")
        @Expose
        private List<String> sizes = null;
        @SerializedName("categories")
        @Expose
        private List<Category> categories = null;

        /**
         * No args constructor for use in serialization
         */
        public Data() {
        }

        /**
         * @param gender
         * @param sizes
         * @param categories
         */
        public Data(List<String> gender, List<String> sizes, List<Category> categories) {
            super();
            this.gender = gender;
            this.sizes = sizes;
            this.categories = categories;
        }

        public List<String> getGender() {
            return gender;
        }

        public void setGender(List<String> gender) {
            this.gender = gender;
        }

        public Data getGender(List<String> gender) {
            this.gender = gender;
            return this;
        }

        public List<String> getSizes() {
            return sizes;
        }

        public void setSizes(List<String> sizes) {
            this.sizes = sizes;
        }

        public Data getSizes(List<String> sizes) {
            this.sizes = sizes;
            return this;
        }

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        public Data withCategories(List<Category> categories) {
            this.categories = categories;
            return this;
        }

    }

}
