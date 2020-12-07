
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OptionValue {

    @SerializedName("productOptionValueId")
    @Expose
    private String productOptionValueId;
    @SerializedName("productOptionValCode")
    @Expose
    private String productOptionValCode;
    @SerializedName("productOptValGenericCode")
    @Expose
    private Object productOptValGenericCode;
    @SerializedName("conversionCode")
    @Expose
    private Object conversionCode;
    @SerializedName("productOptValImage")
    @Expose
    private Object productOptValImage;
    @SerializedName("productOptionValueDescriptions")
    @Expose
    private List<ProductOptionValueDescription> productOptionValueDescriptions = null;

    /**
     * No args constructor for use in serialization
     */
    public OptionValue() {
    }

    /**
     * @param productOptionValueId
     * @param productOptValImage
     * @param productOptValGenericCode
     * @param conversionCode
     * @param productOptionValCode
     * @param productOptionValueDescriptions
     */
    public OptionValue(String productOptionValueId, String productOptionValCode, Object productOptValGenericCode, Object conversionCode, Object productOptValImage, List<ProductOptionValueDescription> productOptionValueDescriptions) {
        super();
        this.productOptionValueId = productOptionValueId;
        this.productOptionValCode = productOptionValCode;
        this.productOptValGenericCode = productOptValGenericCode;
        this.conversionCode = conversionCode;
        this.productOptValImage = productOptValImage;
        this.productOptionValueDescriptions = productOptionValueDescriptions;
    }

    public String getProductOptionValueId() {
        return productOptionValueId;
    }

    public void setProductOptionValueId(String productOptionValueId) {
        this.productOptionValueId = productOptionValueId;
    }

    public OptionValue withProductOptionValueId(String productOptionValueId) {
        this.productOptionValueId = productOptionValueId;
        return this;
    }

    public String getProductOptionValCode() {
        return productOptionValCode;
    }

    public void setProductOptionValCode(String productOptionValCode) {
        this.productOptionValCode = productOptionValCode;
    }

    public OptionValue withProductOptionValCode(String productOptionValCode) {
        this.productOptionValCode = productOptionValCode;
        return this;
    }

    public Object getProductOptValGenericCode() {
        return productOptValGenericCode;
    }

    public void setProductOptValGenericCode(Object productOptValGenericCode) {
        this.productOptValGenericCode = productOptValGenericCode;
    }

    public OptionValue withProductOptValGenericCode(Object productOptValGenericCode) {
        this.productOptValGenericCode = productOptValGenericCode;
        return this;
    }

    public Object getConversionCode() {
        return conversionCode;
    }

    public void setConversionCode(Object conversionCode) {
        this.conversionCode = conversionCode;
    }

    public OptionValue withConversionCode(Object conversionCode) {
        this.conversionCode = conversionCode;
        return this;
    }

    public Object getProductOptValImage() {
        return productOptValImage;
    }

    public void setProductOptValImage(Object productOptValImage) {
        this.productOptValImage = productOptValImage;
    }

    public OptionValue withProductOptValImage(Object productOptValImage) {
        this.productOptValImage = productOptValImage;
        return this;
    }

    public List<ProductOptionValueDescription> getProductOptionValueDescriptions() {
        return productOptionValueDescriptions;
    }

    public void setProductOptionValueDescriptions(List<ProductOptionValueDescription> productOptionValueDescriptions) {
        this.productOptionValueDescriptions = productOptionValueDescriptions;
    }

    public OptionValue withProductOptionValueDescriptions(List<ProductOptionValueDescription> productOptionValueDescriptions) {
        this.productOptionValueDescriptions = productOptionValueDescriptions;
        return this;
    }

}
