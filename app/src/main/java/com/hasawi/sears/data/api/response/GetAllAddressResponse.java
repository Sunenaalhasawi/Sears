
package com.hasawi.sears.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hasawi.sears.data.api.model.pojo.Address;
import com.hasawi.sears.data.api.model.pojo.Pageable;
import com.hasawi.sears.data.api.model.pojo.Sort;

import java.util.List;

public class GetAllAddressResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public class Data {

        @SerializedName("content")
        @Expose
        private List<Address> addresses = null;
        @SerializedName("pageable")
        @Expose
        private Pageable pageable;
        @SerializedName("last")
        @Expose
        private Boolean last;
        @SerializedName("totalPages")
        @Expose
        private Integer totalPages;
        @SerializedName("totalElements")
        @Expose
        private Integer totalElements;
        @SerializedName("sort")
        @Expose
        private Sort sort;
        @SerializedName("first")
        @Expose
        private Boolean first;
        @SerializedName("numberOfElements")
        @Expose
        private Integer numberOfElements;
        @SerializedName("size")
        @Expose
        private Integer size;
        @SerializedName("number")
        @Expose
        private Integer number;
        @SerializedName("empty")
        @Expose
        private Boolean empty;

        public List<Address> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<Address> addresses) {
            this.addresses = addresses;
        }

        public Pageable getPageable() {
            return pageable;
        }

        public void setPageable(Pageable pageable) {
            this.pageable = pageable;
        }

        public Boolean getLast() {
            return last;
        }

        public void setLast(Boolean last) {
            this.last = last;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public Integer getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(Integer totalElements) {
            this.totalElements = totalElements;
        }

        public Sort getSort() {
            return sort;
        }

        public void setSort(Sort sort) {
            this.sort = sort;
        }

        public Boolean getFirst() {
            return first;
        }

        public void setFirst(Boolean first) {
            this.first = first;
        }

        public Integer getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(Integer numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Boolean getEmpty() {
            return empty;
        }

        public void setEmpty(Boolean empty) {
            this.empty = empty;
        }

    }

}
