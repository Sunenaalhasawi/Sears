package com.hasawi.sears_outlet.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hasawi.sears_outlet.data.api.model.pojo.Product;

import java.util.List;

public class SearchedProductDetailsResponse {

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public class Data {
        @SerializedName("product")
        @Expose
        Product product;
        @SerializedName("recommended")
        @Expose
        List<Product> recommendedProductList;

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public List<Product> getRecommendedProductList() {
            return recommendedProductList;
        }

        public void setRecommendedProductList(List<Product> recommendedProductList) {
            this.recommendedProductList = recommendedProductList;
        }
    }

}