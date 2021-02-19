
package com.hasawi.sears_outlet.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductOptionDescription {

    @SerializedName("optionDescriptionId")
    @Expose
    private String optionDescriptionId;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("language")
    @Expose
    private Language language;

    /**
     * No args constructor for use in serialization
     */
    public ProductOptionDescription() {
    }

    /**
     * @param optionDescriptionId
     * @param name
     * @param description
     * @param language
     */
    public ProductOptionDescription(String optionDescriptionId, Object description, String name, Language language) {
        super();
        this.optionDescriptionId = optionDescriptionId;
        this.description = description;
        this.name = name;
        this.language = language;
    }

    public String getOptionDescriptionId() {
        return optionDescriptionId;
    }

    public void setOptionDescriptionId(String optionDescriptionId) {
        this.optionDescriptionId = optionDescriptionId;
    }

    public ProductOptionDescription withOptionDescriptionId(String optionDescriptionId) {
        this.optionDescriptionId = optionDescriptionId;
        return this;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public ProductOptionDescription withDescription(Object description) {
        this.description = description;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductOptionDescription withName(String name) {
        this.name = name;
        return this;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ProductOptionDescription withLanguage(Language language) {
        this.language = language;
        return this;
    }

}
