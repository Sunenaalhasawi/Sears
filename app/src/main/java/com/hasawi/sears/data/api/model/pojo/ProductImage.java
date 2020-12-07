
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductImage {

    @SerializedName("imageId")
    @Expose
    private String imageId;
    @SerializedName("imageName")
    @Expose
    private String imageName;
    @SerializedName("createDate")
    @Expose
    private Object createDate;
    @SerializedName("rank")
    @Expose
    private int rank;

    /**
     * No args constructor for use in serialization
     */
    public ProductImage() {
    }

    /**
     * @param imageId
     * @param imageName
     * @param rank
     * @param createDate
     */
    public ProductImage(String imageId, String imageName, Object createDate, int rank) {
        super();
        this.imageId = imageId;
        this.imageName = imageName;
        this.createDate = createDate;
        this.rank = rank;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public ProductImage withImageId(String imageId) {
        this.imageId = imageId;
        return this;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public ProductImage withImageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public Object getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Object createDate) {
        this.createDate = createDate;
    }

    public ProductImage withCreateDate(Object createDate) {
        this.createDate = createDate;
        return this;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public ProductImage withRank(int rank) {
        this.rank = rank;
        return this;
    }

}
