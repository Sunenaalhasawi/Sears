
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
    private String refSku;
    @SerializedName("oneTimePrice")
    @Expose
    private Double oneTimePrice;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("amount")
    @Expose
    private Integer amount;

    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("productConfigurable")
    @Expose
    private ProductConfigurable productConfigurable;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isOutOfStock() {
        return isOutOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        isOutOfStock = outOfStock;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ProductConfigurable getProductConfigurable() {
        return productConfigurable;
    }

    public void setProductConfigurable(ProductConfigurable productConfigurable) {
        this.productConfigurable = productConfigurable;
    }
}
