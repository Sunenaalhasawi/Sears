
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {

    @SerializedName("content")
    @Expose
    private List<Content> content = null;
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
    public Product() {
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
    public Product(List<Content> content, Pageable pageable, int totalPages, int totalElements, boolean last, Sort sort, boolean first, int numberOfElements, int size, int number, boolean empty) {
        super();
        this.content = content;
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

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public Product withContent(List<Content> content) {
        this.content = content;
        return this;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public Product withPageable(Pageable pageable) {
        this.pageable = pageable;
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public Product withTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public Product withTotalElements(int totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public Product withLast(boolean last) {
        this.last = last;
        return this;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Product withSort(Sort sort) {
        this.sort = sort;
        return this;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public Product withFirst(boolean first) {
        this.first = first;
        return this;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Product withNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
        return this;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Product withSize(int size) {
        this.size = size;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Product withNumber(int number) {
        this.number = number;
        return this;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public Product withEmpty(boolean empty) {
        this.empty = empty;
        return this;
    }

}
