package com.hasawi.sears.data.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Language {

    @SerializedName("languageId")
    @Expose
    private String languageId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("active")
    @Expose
    private boolean active;

    /**
     * No args constructor for use in serialization
     */
    public Language() {
    }

    /**
     * @param code
     * @param languageId
     * @param name
     * @param active
     */
    public Language(String languageId, String code, String name, boolean active) {
        super();
        this.languageId = languageId;
        this.code = code;
        this.name = name;
        this.active = active;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public Language withLanguageId(String languageId) {
        this.languageId = languageId;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Language withCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Language withName(String name) {
        this.name = name;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Language withActive(boolean active) {
        this.active = active;
        return this;
    }

}