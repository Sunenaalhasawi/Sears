
package com.hasawi.sears_outlet.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hasawi.sears_outlet.data.api.model.pojo.Data;

public class ProductResponse {

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
    private Throwable error;

    /**
     * No args constructor for use in serialization
     */
    public ProductResponse() {
    }

    /**
     * @param data
     * @param message
     * @param status
     * @param statusCode
     */
    public ProductResponse(String status, String message, Data data, int statusCode) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
        this.error = null;

    }

    public ProductResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductResponse withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProductResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public ProductResponse withData(Data data) {
        this.data = data;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ProductResponse withStatusCode(int statusCode) {
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
