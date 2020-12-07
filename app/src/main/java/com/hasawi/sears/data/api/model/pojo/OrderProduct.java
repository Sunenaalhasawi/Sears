
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderProduct {

    @SerializedName("orderProductId")
    @Expose
    private Object orderProductId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("sku")
    @Expose
    private Object sku;
    @SerializedName("refSku")
    @Expose
    private Object refSku;
    @SerializedName("oneTimePrice")
    @Expose
    private Double oneTimePrice;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    public Object getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Object orderProductId) {
        this.orderProductId = orderProductId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Object getSku() {
        return sku;
    }

    public void setSku(Object sku) {
        this.sku = sku;
    }

    public Object getRefSku() {
        return refSku;
    }

    public void setRefSku(Object refSku) {
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

}
