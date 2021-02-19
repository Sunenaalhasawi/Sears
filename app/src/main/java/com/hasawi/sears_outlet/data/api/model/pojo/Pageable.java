
package com.hasawi.sears_outlet.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pageable {

    @SerializedName("sort")
    @Expose
    private Sort sort;
    @SerializedName("pageNumber")
    @Expose
    private int pageNumber;
    @SerializedName("pageSize")
    @Expose
    private int pageSize;
    @SerializedName("offset")
    @Expose
    private int offset;
    @SerializedName("unpaged")
    @Expose
    private boolean unpaged;
    @SerializedName("paged")
    @Expose
    private boolean paged;

    /**
     * No args constructor for use in serialization
     */
    public Pageable() {
    }

    /**
     * @param paged
     * @param pageNumber
     * @param offset
     * @param pageSize
     * @param unpaged
     * @param sort
     */
    public Pageable(Sort sort, int pageNumber, int pageSize, int offset, boolean unpaged, boolean paged) {
        super();
        this.sort = sort;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.offset = offset;
        this.unpaged = unpaged;
        this.paged = paged;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Pageable withSort(Sort sort) {
        this.sort = sort;
        return this;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Pageable withPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Pageable withPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Pageable withOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public boolean isUnpaged() {
        return unpaged;
    }

    public void setUnpaged(boolean unpaged) {
        this.unpaged = unpaged;
    }

    public Pageable withUnpaged(boolean unpaged) {
        this.unpaged = unpaged;
        return this;
    }

    public boolean isPaged() {
        return paged;
    }

    public void setPaged(boolean paged) {
        this.paged = paged;
    }

    public Pageable withPaged(boolean paged) {
        this.paged = paged;
        return this;
    }

}
