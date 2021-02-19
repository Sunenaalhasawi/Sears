
package com.hasawi.sears_outlet.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderProduct {

    @SerializedName("orderProductId")
    @Expose
    private String orderProductId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("refSku")
    @Expose
    private String refSku;
    @SerializedName("oneTimePrice")
    @Expose
    private Double oneTimePrice;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("productImage")
    @Expose
    private String productImage;
    @SerializedName("productSize")
    @Expose
    private String productSize;
    @SerializedName("productColorCode")
    @Expose
    private String productColorCode;

    public String getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(String orderProductId) {
        this.orderProductId = orderProductId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getRefSku() {
        return refSku;
    }

    public void setRefSku(String refSku) {
        this.refSku = refSku;
    }

    public Double getOneTimePrice() {
        return oneTimePrice;
    }

    public void setOneTimePrice(Double oneTimePrice) {
        this.oneTimePrice = oneTimePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductColorCode() {
        return productColorCode;
    }

    public void setProductColorCode(String productColorCode) {
        this.productColorCode = productColorCode;
    }
}
