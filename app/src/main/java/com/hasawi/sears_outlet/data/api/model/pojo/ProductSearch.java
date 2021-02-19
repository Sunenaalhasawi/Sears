package com.hasawi.sears_outlet.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductSearch {

    @SerializedName("objectID")
    @Expose
    private String objectID;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("nameEn")
    @Expose
    private String nameEn;
    @SerializedName("nameAr")
    @Expose
    private String nameAr;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("itemNos")
    @Expose
    private List<ItemNo> itemNoList;

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<ItemNo> getItemNoList() {
        return itemNoList;
    }

    public void setItemNoList(List<ItemNo> itemNoList) {
        this.itemNoList = itemNoList;
    }

    class ItemNo {

        @SerializedName("itemNo")
        @Expose
        private String itemNo;
        @SerializedName("qty")
        @Expose
        private Integer quantity;

        public String getItemNo() {
            return itemNo;
        }

        public void setItemNo(String itemNo) {
            this.itemNo = itemNo;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

}
