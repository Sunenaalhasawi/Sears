package com.hasawi.sears_outlet.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hasawi.sears_outlet.data.api.model.User;
import com.hasawi.sears_outlet.data.api.model.pojo.Cart;
import com.hasawi.sears_outlet.data.api.model.pojo.PaymentMode;
import com.hasawi.sears_outlet.data.api.model.pojo.ShippingMode;

import java.util.List;

public class CheckoutResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data checkoutData;
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

    public Data getCheckoutData() {
        return checkoutData;
    }

    public void setCheckoutData(Data checkoutData) {
        this.checkoutData = checkoutData;
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
        @SerializedName("payments")
        @Expose
        private List<PaymentMode> paymentModes;
        @SerializedName("shippings")
        @Expose
        private List<ShippingMode> shippingModes;
        @SerializedName("customer")
        @Expose
        private User user;
        @SerializedName("shoppingCart")
        @Expose
        private Cart shoppingCart;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public List<PaymentMode> getPaymentModes() {
            return paymentModes;
        }

        public void setPaymentModes(List<PaymentMode> paymentModes) {
            this.paymentModes = paymentModes;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Cart getShoppingCart() {
            return shoppingCart;
        }

        public void setShoppingCart(Cart shoppingCart) {
            this.shoppingCart = shoppingCart;
        }

        public List<ShippingMode> getShippingModes() {
            return shippingModes;
        }

        public void setShippingModes(List<ShippingMode> shippingModes) {
            this.shippingModes = shippingModes;
        }
    }

}
