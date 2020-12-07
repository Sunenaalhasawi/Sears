
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductOptionValueDescription {

    @SerializedName("productOptionValueDescriptionId")
    @Expose
    private String productOptionValueDescriptionId;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private Object title;
    @SerializedName("language")
    @Expose
    private Language language;

    /**
     * No args constructor for use in serialization
     */
    public ProductOptionValueDescription() {
    }

    /**
     * @param productOptionValueDescriptionId
     * @param name
     * @param description
     * @param language
     * @param title
     */
    public ProductOptionValueDescription(String productOptionValueDescriptionId, Object description, String name, Object title, Language language) {
        super();
        this.productOptionValueDescriptionId = productOptionValueDescriptionId;
        this.description = description;
        this.name = name;
        this.title = title;
        this.language = language;
    }

    public String getProductOptionValueDescriptionId() {
        return productOptionValueDescriptionId;
    }

    public void setProductOptionValueDescriptionId(String productOptionValueDescriptionId) {
        this.productOptionValueDescriptionId = productOptionValueDescriptionId;
    }

    public ProductOptionValueDescription withProductOptionValueDescriptionId(String productOptionValueDescriptionId) {
        this.productOptionValueDescriptionId = productOptionValueDescriptionId;
        return this;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public ProductOptionValueDescription withDescription(Object description) {
        this.description = description;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductOptionValueDescription withName(String name) {
        this.name = name;
        return this;
    }

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

    public ProductOptionValueDescription withTitle(Object title) {
        this.title = title;
        return this;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ProductOptionValueDescription withLanguage(Language language) {
        this.language = language;
        return this;
    }

}
