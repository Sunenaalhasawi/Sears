
package com.hasawi.sears_outlet.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductListData {

    @SerializedName("content")
    @Expose
    private List<Product> products = null;
    @SerializedName("pageable")
    @Expose
    private Pageable pageable;
    @SerializedName("totalPages")
    @Expose
    private int totalPages;
    @SerializedName("totalElements")
    @Expose
    private int totalElements;
    @SerializedName("last")
    @Expose
    private boolean last;
    @SerializedName("sort")
    @Expose
    private Sort sort;
    @SerializedName("first")
    @Expose
    private boolean first;
    @SerializedName("numberOfElements")
    @Expose
    private int numberOfElements;
    @SerializedName("size")
    @Expose
    private int size;
    @SerializedName("number")
    @Expose
    private int number;
    @SerializedName("empty")
    @Expose
    private boolean empty;

    /**
     * No args constructor for use in serialization
     */
    public ProductListData() {
    }

    /**
     * @param number
     * @param last
     * @param numberOfElements
     * @param size
     * @param totalPages
     * @param pageable
     * @param sort
     * @param content
     * @param first
     * @param totalElements
     * @param empty
     */
    public ProductListData(List<Product> content, Pageable pageable, int totalPages, int totalElements, boolean last, Sort sort, boolean first, int numberOfElements, int size, int number, boolean empty) {
        super();
        this.products = content;
        this.pageable = pageable;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.last = last;
        this.sort = sort;
        this.first = first;
        this.numberOfElements = numberOfElements;
        this.size = size;
        this.number = number;
        this.empty = empty;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public ProductListData withContent(List<Product> content) {
        this.products = content;
        return this;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public ProductListData withPageable(Pageable pageable) {
        this.pageable = pageable;
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public ProductListData withTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public ProductListData withTotalElements(int totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public ProductListData withLast(boolean last) {
        this.last = last;
        return this;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public ProductListData withSort(Sort sort) {
        this.sort = sort;
        return this;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public ProductListData withFirst(boolean first) {
        this.first = first;
        return this;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public ProductListData withNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
        return this;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ProductListData withSize(int size) {
        this.size = size;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ProductListData withNumber(int number) {
        this.number = number;
        return this;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public ProductListData withEmpty(boolean empty) {
        this.empty = empty;
        return this;
    }

}
