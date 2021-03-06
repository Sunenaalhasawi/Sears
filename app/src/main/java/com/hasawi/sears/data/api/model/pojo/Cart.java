package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cart {

    @SerializedName("shoppingCartId")
    @Expose
    private String shoppingCartId;
    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("dateModified")
    @Expose
    private String dateModified;
    @SerializedName("subTotal")
    @Expose
    private Double subTotal;
    @SerializedName("total")
    @Expose
    private Object total;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("couponCode")
    @Expose
    private Object couponCode;
    @SerializedName("shoppingCartCode")
    @Expose
    private Object shoppingCartCode;
    @SerializedName("itemCount")
    @Expose
    private int itemCount;
    @SerializedName("shoppingCartItems")
    @Expose
    private List<ShoppingCartItem> shoppingCartItems = null;
    @SerializedName("available")
    @Expose
    private List<ShoppingCartItem> available = null;
    @SerializedName("outOfStock")
    @Expose
    private List<ShoppingCartItem> outOfStock = null;

    public String getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(String shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    //
    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Object getTotal() {
        return total;
    }

    public void setTotal(Object total) {
        this.total = total;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Object getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(Object couponCode) {
        this.couponCode = couponCode;
    }

    public Object getShoppingCartCode() {
        return shoppingCartCode;
    }

    public void setShoppingCartCode(Object shoppingCartCode) {
        this.shoppingCartCode = shoppingCartCode;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public List<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void setShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }

    public List<ShoppingCartItem> getAvailable() {
        return available;
    }

    public void setAvailable(List<ShoppingCartItem> available) {
        this.available = available;
    }

    public List<ShoppingCartItem> getOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(List<ShoppingCartItem> outOfStock) {
        this.outOfStock = outOfStock;
    }

}
