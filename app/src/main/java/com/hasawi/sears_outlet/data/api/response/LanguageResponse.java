package com.hasawi.sears_outlet.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hasawi.sears_outlet.data.api.model.Language;

import java.util.List;

public class LanguageResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Language> data = null;
    @SerializedName("statusCode")
    @Expose
    private int statusCode;

    private Throwable error;

    /**
     * No args constructor for use in serialization
     */
    public LanguageResponse() {
    }

    /**
     * @param data
     * @param message
     * @param status
     * @param statusCode
     */
    public LanguageResponse(String status, String message, List<Language> data, int statusCode) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
        this.error = null;
    }

    public LanguageResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LanguageResponse withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LanguageResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Language> getData() {
        return data;
    }

    public void setData(List<Language> data) {
        this.data = data;
    }

    public LanguageResponse withData(List<Language> data) {
        this.data = data;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public LanguageResponse withStatusCode(int statusCode) {
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