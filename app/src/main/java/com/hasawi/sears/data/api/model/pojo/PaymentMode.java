package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentMode {

    @SerializedName("orderId")
    @Expose
    private String OrderId;
    @SerializedName("paymentId")
    @Expose
    private Object paymentId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("paymentUrl")
    @Expose
    private String paymentUrl;

    @SerializedName("successUrl")
    @Expose
    private String successUrl;

    @SerializedName("failedUrl")
    @Expose
    private String failedUrl;

    @SerializedName("postPay")
    @Expose
    private boolean postPay;

    @SerializedName("iconUrl")
    @Expose
    private String icon;

    @SerializedName("paymentMethodId")
    @Expose
    private String paymentMethodId;

    @SerializedName("active")
    @Expose
    private boolean active;

    public Object getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Object paymentId) {
        this.paymentId = paymentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getFailedUrl() {
        return failedUrl;
    }

    public void setFailedUrl(String failedUrl) {
        this.failedUrl = failedUrl;
    }

    public boolean isPostPay() {
        return postPay;
    }

    public void setPostPay(boolean postPay) {
        this.postPay = postPay;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
