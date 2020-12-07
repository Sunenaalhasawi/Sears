package com.hasawi.sears.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressResponse {

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

        @SerializedName("addressId")
        @Expose
        private String addressId;
        @SerializedName("street")
        @Expose
        private String street;
        @SerializedName("area")
        @Expose
        private String area;
        @SerializedName("flat")
        @Expose
        private String flat;
        @SerializedName("block")
        @Expose
        private String block;
        @SerializedName("customerId")
        @Expose
        private String customerId;
        @SerializedName("country")
        @Expose
        private String country;

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getFlat() {
            return flat;
        }

        public void setFlat(String flat) {
            this.flat = flat;
        }

        public String getBlock() {
            return block;
        }

        public void setBlock(String block) {
            this.block = block;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

    }

}