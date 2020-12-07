
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Description {

    @SerializedName("categoryDescriptionId")
    @Expose
    private String categoryDescriptionId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("language")
    @Expose
    private Language language;

    /**
     * No args constructor for use in serialization
     */
    public Description() {
    }

    /**
     * @param imageUrl
     * @param categoryDescriptionId
     * @param language
     * @param categoryName
     * @param slug
     */
    public Description(String categoryDescriptionId, String categoryName, String slug, String imageUrl, Language language) {
        super();
        this.categoryDescriptionId = categoryDescriptionId;
        this.categoryName = categoryName;
        this.slug = slug;
        this.imageUrl = imageUrl;
        this.language = language;
    }

    public String getCategoryDescriptionId() {
        return categoryDescriptionId;
    }

    public void setCategoryDescriptionId(String categoryDescriptionId) {
        this.categoryDescriptionId = categoryDescriptionId;
    }

    public Description withCategoryDescriptionId(String categoryDescriptionId) {
        this.categoryDescriptionId = categoryDescriptionId;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Description withCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Description withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Description withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Description withLanguage(Language language) {
        this.language = language;
        return this;
    }

}
