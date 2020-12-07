package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wishlist {
    @SerializedName("wishListId")
    @Expose
    private String wishListId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("product")
    @Expose
    private Content product;
    @SerializedName("customerId")
    @Expose
    private String customerId;

    public String getWishListId() {
        return wishListId;
    }

    public void setWishListId(String wishListId) {
        this.wishListId = wishListId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Content getProduct() {
        return product;
    }

    public void setProduct(Content product) {
        this.product = product;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

}