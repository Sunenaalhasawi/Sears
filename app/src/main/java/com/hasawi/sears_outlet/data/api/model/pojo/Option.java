
package com.hasawi.sears_outlet.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Option {

    @SerializedName("productOptionId")
    @Expose
    private String productOptionId;
    @SerializedName("productOptionCode")
    @Expose
    private String productOptionCode;
    @SerializedName("productOptionType")
    @Expose
    private String productOptionType;
    @SerializedName("productOptionDescriptions")
    @Expose
    private List<ProductOptionDescription> productOptionDescriptions = null;

    /**
     * No args constructor for use in serialization
     */
    public Option() {
    }

    /**
     * @param productOptionId
     * @param productOptionDescriptions
     * @param productOptionCode
     * @param productOptionType
     */
    public Option(String productOptionId, String productOptionCode, String productOptionType, List<ProductOptionDescription> productOptionDescriptions) {
        super();
        this.productOptionId = productOptionId;
        this.productOptionCode = productOptionCode;
        this.productOptionType = productOptionType;
        this.productOptionDescriptions = productOptionDescriptions;
    }

    public String getProductOptionId() {
        return productOptionId;
    }

    public void setProductOptionId(String productOptionId) {
        this.productOptionId = productOptionId;
    }

    public Option withProductOptionId(String productOptionId) {
        this.productOptionId = productOptionId;
        return this;
    }

    public String getProductOptionCode() {
        return productOptionCode;
    }

    public void setProductOptionCode(String productOptionCode) {
        this.productOptionCode = productOptionCode;
    }

    public Option withProductOptionCode(String productOptionCode) {
        this.productOptionCode = productOptionCode;
        return this;
    }

    public String getProductOptionType() {
        return productOptionType;
    }

    public void setProductOptionType(String productOptionType) {
        this.productOptionType = productOptionType;
    }

    public Option withProductOptionType(String productOptionType) {
        this.productOptionType = productOptionType;
        return this;
    }

    public List<ProductOptionDescription> getProductOptionDescriptions() {
        return productOptionDescriptions;
    }

    public void setProductOptionDescriptions(List<ProductOptionDescription> productOptionDescriptions) {
        this.productOptionDescriptions = productOptionDescriptions;
    }

    public Option withProductOptionDescriptions(List<ProductOptionDescription> productOptionDescriptions) {
        this.productOptionDescriptions = productOptionDescriptions;
        return this;
    }

}
