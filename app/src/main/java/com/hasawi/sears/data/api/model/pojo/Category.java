
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {

    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("categoryCode")
    @Expose
    private String categoryCode;
    @SerializedName("depth")
    @Expose
    private int depth;
    @SerializedName("active")
    @Expose
    private boolean active;
    @SerializedName("parentId")
    @Expose
    private String parentId;
    @SerializedName("lineAge")
    @Expose
    private String lineAge;
    @SerializedName("categories")
    @Expose
    private List<Object> categories = null;
    @SerializedName("descriptions")
    @Expose
    private List<Description> descriptions = null;

    /**
     * No args constructor for use in serialization
     */
    public Category() {
    }

    /**
     * @param depth
     * @param active
     * @param categoryCode
     * @param categories
     * @param descriptions
     * @param categoryId
     * @param parentId
     */
    public Category(String categoryId, String categoryCode, int depth, boolean active, String parentId, List<Object> categories, List<Description> descriptions) {
        super();
        this.categoryId = categoryId;
        this.categoryCode = categoryCode;
        this.depth = depth;
        this.active = active;
        this.parentId = parentId;
        this.categories = categories;
        this.descriptions = descriptions;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Category withCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Category withCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
        return this;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Category withDepth(int depth) {
        this.depth = depth;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Category withActive(boolean active) {
        this.active = active;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Category withParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public List<Object> getCategories() {
        return categories;
    }

    public void setCategories(List<Object> categories) {
        this.categories = categories;
    }

    public Category withCategories(List<Object> categories) {
        this.categories = categories;
        return this;
    }

    public String getLineAge() {
        return lineAge;
    }

    public void setLineAge(String lineAge) {
        this.lineAge = lineAge;
    }


    public List<Description> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<Description> descriptions) {
        this.descriptions = descriptions;
    }

    public Category withDescriptions(List<Description> descriptions) {
        this.descriptions = descriptions;
        return this;
    }

}
