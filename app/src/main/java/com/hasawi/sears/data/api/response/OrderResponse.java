
package com.hasawi.sears.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hasawi.sears.data.api.model.pojo.Invoice;
import com.hasawi.sears.data.api.model.pojo.OrderProduct;
import com.hasawi.sears.data.api.model.pojo.PaymentMode;

import java.util.List;

public class OrderResponse {

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

        @SerializedName("orderId")
        @Expose
        private String orderId;
        @SerializedName("dateOfPurchase")
        @Expose
        private String dateOfPurchase;
        @SerializedName("customerId")
        @Expose
        private String customerId;
        @SerializedName("addressId")
        @Expose
        private String addressId;
        @SerializedName("total")
        @Expose
        private Double total;
        @SerializedName("discounted")
        @Expose
        private Object discounted;
        @SerializedName("couponCode")
        @Expose
        private Object couponCode;
        @SerializedName("orderProducts")
        @Expose
        private List<OrderProduct> orderProducts = null;
        @SerializedName("payment")
        @Expose
        private PaymentMode payment;
        @SerializedName("getPaymentStatusResponse")
        @Expose
        private Invoice invoiceData;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getDateOfPurchase() {
            return dateOfPurchase;
        }

        public void setDateOfPurchase(String dateOfPurchase) {
            this.dateOfPurchase = dateOfPurchase;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public Double getTotal() {
            return total;
        }

        public void setTotal(Double total) {
            this.total = total;
        }

        public Object getDiscounted() {
            return discounted;
        }

        public void setDiscounted(Object discounted) {
            this.discounted = discounted;
        }

        public Object getCouponCode() {
            return couponCode;
        }

        public void setCouponCode(Object couponCode) {
            this.couponCode = couponCode;
        }

        public List<OrderProduct> getOrderProducts() {
            return orderProducts;
        }

        public void setOrderProducts(List<OrderProduct> orderProducts) {
            this.orderProducts = orderProducts;
        }

        public PaymentMode getPayment() {
            return payment;
        }

        public void setPayment(PaymentMode payment) {
            this.payment = payment;
        }

        public Invoice getInvoiceData() {
            return invoiceData;
        }

        public void setInvoiceData(Invoice invoiceData) {
            this.invoiceData = invoiceData;
        }

    }


}
