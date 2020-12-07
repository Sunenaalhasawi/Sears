
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDescription {

    @SerializedName("productDescriptionId")
    @Expose
    private String productDescriptionId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("description")
    @Expose
    private String productDescription;

    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("language")
    @Expose
    private Language language;

    /**
     * No args constructor for use in serialization
     */
    public ProductDescription() {
    }

    /**
     * @param productDescriptionId
     * @param language
     * @param productName
     * @param slug
     */
    public ProductDescription(String productDescriptionId, String productDescription, String productName, String slug, Language language) {
        super();
        this.productDescriptionId = productDescriptionId;
        this.productName = productName;
        this.slug = slug;
        this.productDescription = productDescription;
        this.language = language;
    }

    public String getProductDescriptionId() {
        return productDescriptionId;
    }

    public void setProductDescriptionId(String productDescriptionId) {
        this.productDescriptionId = productDescriptionId;
    }

    public ProductDescription withProductDescriptionId(String productDescriptionId) {
        this.productDescriptionId = productDescriptionId;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductDescription withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public ProductDescription withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ProductDescription withLanguage(Language language) {
        this.language = language;
        return this;
    }

}
