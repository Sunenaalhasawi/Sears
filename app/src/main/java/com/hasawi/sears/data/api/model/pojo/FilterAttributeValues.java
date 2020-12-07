package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterAttributeValues {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private Object code;
    //
    @SerializedName("checked")
    @Expose
    private boolean checked;

    /**
     * No args constructor for use in serialization
     */
    public FilterAttributeValues() {
    }

    /**
     * @param code
     * @param name
     * @param id
     */
    public FilterAttributeValues(String id, String name, Object code) {
        super();
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FilterAttributeValues withId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FilterAttributeValues withName(String name) {
        this.name = name;
        return this;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public FilterAttributeValues withCode(Object code) {
        this.code = code;
        return this;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
