
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Data {

    @SerializedName("attributes")
    @Expose
    private Map<String, List<FilterAttributeValues>> filterAttributes = null;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName(("sort"))
    @Expose
    private List<String> sortStrings = null;
    @SerializedName("products")
    @Expose
    private Product products;

    /**
     * No args constructor for use in serialization
     */
    public Data() {
    }

    /**
     * @param attributes
     * @param categories
     * @param products
     */
    public Data(Map<String, List<FilterAttributeValues>> attributes, List<Category> categories, List<String> sortStrings, Product products) {
        super();
        this.filterAttributes = attributes;
        this.categories = categories;
        this.sortStrings = sortStrings;
        this.products = products;
    }

    public Map<String, List<FilterAttributeValues>> getFilterAttributes() {
        return filterAttributes;
    }

    public void setFilterAttributes(Map<String, List<FilterAttributeValues>> filterAttributes) {
        this.filterAttributes = filterAttributes;
    }

    public Data withAttributes(Map<String, List<FilterAttributeValues>> attributes) {
        this.filterAttributes = attributes;
        return this;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Data withCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Product getProducts() {
        return products;
    }

    public void setProducts(Product products) {
        this.products = products;
    }

    public Data withProducts(Product products) {
        this.products = products;
        return this;
    }

    public List<String> getSortStrings() {
        return sortStrings;
    }

    public void setSortStrings(List<String> sortStrings) {
        this.sortStrings = sortStrings;
    }
}