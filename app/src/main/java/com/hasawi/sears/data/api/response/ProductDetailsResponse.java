
package com.hasawi.sears.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hasawi.sears.data.api.model.pojo.Product;

public class ProductDetailsResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Product data;
    @SerializedName("statusCode")
    @Expose
    private int statusCode;

    private Throwable error;

    /**
     * No args constructor for use in serialization
     */
    public ProductDetailsResponse() {
    }

    /**
     * @param data
     * @param message
     * @param status
     * @param statusCode
     */
    public ProductDetailsResponse(String status, String message, Product data, int statusCode) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
        this.error = null;
    }

    public ProductDetailsResponse(Throwable error) {
        this.data = null;
        this.error = null;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductDetailsResponse withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProductDetailsResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public Product getData() {
        return data;
    }

    public void setData(Product data) {
        this.data = data;
    }

    public ProductDetailsResponse withData(Product data) {
        this.data = data;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ProductDetailsResponse withStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
