
package com.hasawi.sears.data.api.model.pojo;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {

    public static DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.productId == newItem.productId;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.equals(newItem);
        }
    };
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("productType")
    @Expose
    private String productType;
    @SerializedName("descriptions")
    @Expose
    private List<ProductDescription> descriptions;
    @SerializedName("productGroup")
    @Expose
    private Object productGroup;
    @SerializedName("productAttributes")
    @Expose
    private List<ProductAttribute> productAttributes = null;
    @SerializedName("productConfigurables")
    @Expose
    private List<ProductConfigurable> productConfigurables = null;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("brandLogoUrl")
    @Expose
    private String brandLogoUrl;
    @SerializedName("originalPrice")
    @Expose
    private Double originalPrice;
    @SerializedName("discountPrice")
    @Expose
    private Double discountPrice;
    @SerializedName("discountPercentage")
    @Expose
    private Integer discountPercentage;
    @SerializedName("wishlist")
    @Expose
    private boolean wishlist;
    @SerializedName("available")
    @Expose
    private boolean available;
    @SerializedName("discount")
    @Expose
    private boolean discount;
    @SerializedName("manufature")
    @Expose
    private Manufacture manufature;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public List<ProductDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<ProductDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public Object getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(Object productGroup) {
        this.productGroup = productGroup;
    }

    public List<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(List<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getBrandLogoUrl() {
        return brandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl) {
        this.brandLogoUrl = brandLogoUrl;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public List<ProductConfigurable> getProductConfigurables() {
        return productConfigurables;
    }

    public void setProductConfigurables(List<ProductConfigurable> productConfigurables) {
        this.productConfigurables = productConfigurables;
    }

    public Manufacture getManufature() {
        return manufature;
    }

    public void setManufature(Manufacture manufature) {
        this.manufature = manufature;
    }

    public Boolean getDiscount() {
        return discount;
    }

    public boolean getWishlist() {
        return wishlist;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isWishlist() {
        return wishlist;
    }

    public void setWishlist(boolean wishlist) {
        this.wishlist = wishlist;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(Boolean discount) {
        this.discount = discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }


}
