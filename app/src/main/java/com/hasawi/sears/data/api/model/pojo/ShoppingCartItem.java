
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShoppingCartItem {

    @SerializedName("shoppingCartItemId")
    @Expose
    private String shoppingCartItemId;
    //    @SerializedName("dateCreated")
//    @Expose
//    private Object dateCreated;
//    @SerializedName("dateModified")
//    @Expose
//    private Object dateModified;
    @SerializedName("updtId")
    @Expose
    private Object updtId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("refSku")
    @Expose
    private Object refSku;
    @SerializedName("oneTimePrice")
    @Expose
    private Double oneTimePrice;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("product")
    @Expose
    private Content product;

    private boolean isOutOfStock;

    public String getShoppingCartItemId() {
        return shoppingCartItemId;
    }

    public void setShoppingCartItemId(String shoppingCartItemId) {
        this.shoppingCartItemId = shoppingCartItemId;
    }

//    public Object getDateCreated() {
//        return dateCreated;
//    }
//
//    public void setDateCreated(Object dateCreated) {
//        this.dateCreated = dateCreated;
//    }
//
//    public Object getDateModified() {
//        return dateModified;
//    }
//
//    public void setDateModified(Integer dateModified) {
//        this.dateModified = dateModified;
//    }

    public Object getUpdtId() {
        return updtId;
    }

    public void setUpdtId(Object updtId) {
        this.updtId = updtId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public Content getProduct() {
        return product;
    }

    public void setProduct(Content product) {
        this.product = product;
    }

    public boolean isOutOfStock() {
        return isOutOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        isOutOfStock = outOfStock;
    }
}
