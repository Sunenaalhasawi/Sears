
package com.hasawi.sears_outlet.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductAttribute {

    @SerializedName("attributeId")
    @Expose
    private String attributeId;
    @SerializedName("option")
    @Expose
    private Option option;
    @SerializedName("optionValue")
    @Expose
    private OptionValue optionValue;
    @SerializedName("referSku")
    @Expose
    private Object referSku;
    @SerializedName("qty")
    @Expose
    private Object qty;
    @SerializedName("price")
    @Expose
    private Object price;

    /**
     * No args constructor for use in serialization
     */
    public ProductAttribute() {
    }

    /**
     * @param attributeId
     * @param referSku
     * @param price
     * @param optionValue
     * @param qty
     * @param option
     */
    public ProductAttribute(String attributeId, Option option, OptionValue optionValue, Object referSku, Object qty, Object price) {
        super();
        this.attributeId = attributeId;
        this.option = option;
        this.optionValue = optionValue;
        this.referSku = referSku;
        this.qty = qty;
        this.price = price;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public ProductAttribute withAttributeId(String attributeId) {
        this.attributeId = attributeId;
        return this;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public ProductAttribute withOption(Option option) {
        this.option = option;
        return this;
    }

    public OptionValue getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(OptionValue optionValue) {
        this.optionValue = optionValue;
    }

    public ProductAttribute withOptionValue(OptionValue optionValue) {
        this.optionValue = optionValue;
        return this;
    }

    public Object getReferSku() {
        return referSku;
    }

    public void setReferSku(Object referSku) {
        this.referSku = referSku;
    }

    public ProductAttribute withReferSku(Object referSku) {
        this.referSku = referSku;
        return this;
    }

    public Object getQty() {
        return qty;
    }

    public void setQty(Object qty) {
        this.qty = qty;
    }

    public ProductAttribute withQty(Object qty) {
        this.qty = qty;
        return this;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public ProductAttribute withPrice(Object price) {
        this.price = price;
        return this;
    }

}
