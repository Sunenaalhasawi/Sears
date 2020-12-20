package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
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
    private Double discounted;
    @SerializedName("couponCode")
    @Expose
    private String couponCode;
    @SerializedName("paymentType")
    @Expose
    private String paymentType;
    @SerializedName("orderProducts")
    @Expose
    private List<OrderProduct> orderProducts = null;
    @SerializedName("payment")
    @Expose
    private PaymentMode payment;
    @SerializedName("getPaymentStatusResponse")
    @Expose
    private Invoice getPaymentStatusResponse;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("orderTracks")
    @Expose
    private List<OrderTrack> orderTrackList;
    @SerializedName("shippingId")
    @Expose
    private String shippingId;
    @SerializedName("shippingCharge")
    @Expose
    private Double shippingCharge;
    @SerializedName("paymentId")
    @Expose
    private String paymentId;

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

    public Double getDiscounted() {
        return discounted;
    }

    public void setDiscounted(Double discounted) {
        this.discounted = discounted;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
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

    public Invoice getGetPaymentStatusResponse() {
        return getPaymentStatusResponse;
    }

    public void setGetPaymentStatusResponse(Invoice getPaymentStatusResponse) {
        this.getPaymentStatusResponse = getPaymentStatusResponse;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderTrack> getOrderTrackList() {
        return orderTrackList;
    }

    public void setOrderTrackList(List<OrderTrack> orderTrackList) {
        this.orderTrackList = orderTrackList;
    }

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public Double getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(Double shippingCharge) {
        this.shippingCharge = shippingCharge;
    }
}