
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetailsData {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("active")
    @Expose
    private boolean active;
    @SerializedName("productType")
    @Expose
    private String productType;
    @SerializedName("descriptions")
    @Expose
    private List<Description> descriptions = null;
    @SerializedName("productGroup")
    @Expose
    private Object productGroup;
    @SerializedName("productImages")
    @Expose
    private List<ProductImage> productImages = null;
    @SerializedName("productAttributes")
    @Expose
    private List<ProductAttribute> productAttributes = null;
    @SerializedName("categories")
    @Expose
    private List<Object> categories = null;

    /**
     * No args constructor for use in serialization
     */
    public ProductDetailsData() {
    }

    /**
     * @param productImages
     * @param productId
     * @param productGroup
     * @param productAttributes
     * @param active
     * @param categories
     * @param sku
     * @param descriptions
     * @param productType
     */
    public ProductDetailsData(String productId, String sku, boolean active, String productType, List<Description> descriptions, Object productGroup, List<ProductImage> productImages, List<ProductAttribute> productAttributes, List<Object> categories) {
        super();
        this.productId = productId;
        this.sku = sku;
        this.active = active;
        this.productType = productType;
        this.descriptions = descriptions;
        this.productGroup = productGroup;
        this.productImages = productImages;
        this.productAttributes = productAttributes;
        this.categories = categories;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ProductDetailsData withProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public ProductDetailsData withSku(String sku) {
        this.sku = sku;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ProductDetailsData withActive(boolean active) {
        this.active = active;
        return this;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public ProductDetailsData withProductType(String productType) {
        this.productType = productType;
        return this;
    }

    public List<Description> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<Description> descriptions) {
        this.descriptions = descriptions;
    }

    public ProductDetailsData withDescriptions(List<Description> descriptions) {
        this.descriptions = descriptions;
        return this;
    }

    public Object getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(Object productGroup) {
        this.productGroup = productGroup;
    }

    public ProductDetailsData withProductGroup(Object productGroup) {
        this.productGroup = productGroup;
        return this;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public ProductDetailsData withProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
        return this;
    }

    public List<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(List<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public ProductDetailsData withProductAttributes(List<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
        return this;
    }

    public List<Object> getCategories() {
        return categories;
    }

    public void setCategories(List<Object> categories) {
        this.categories = categories;
    }

    public ProductDetailsData withCategories(List<Object> categories) {
        this.categories = categories;
        return this;
    }

}
