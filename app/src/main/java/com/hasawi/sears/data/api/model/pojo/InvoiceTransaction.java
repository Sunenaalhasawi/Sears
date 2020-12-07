
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceTransaction {

    @SerializedName("TransactionDate")
    @Expose
    private Integer transactionDate;
    @SerializedName("PaymentGateway")
    @Expose
    private String paymentGateway;
    @SerializedName("ReferenceId")
    @Expose
    private String referenceId;
    @SerializedName("TrackId")
    @Expose
    private String trackId;
    @SerializedName("TransactionId")
    @Expose
    private String transactionId;
    @SerializedName("PaymentId")
    @Expose
    private String paymentId;
    @SerializedName("AuthorizationId")
    @Expose
    private String authorizationId;
    @SerializedName("TransactionStatus")
    @Expose
    private String transactionStatus;
    @SerializedName("TransationValue")
    @Expose
    private String transationValue;
    @SerializedName("CustomerServiceCharge")
    @Expose
    private String customerServiceCharge;
    @SerializedName("DueValue")
    @Expose
    private String dueValue;
    @SerializedName("PaidCurrency")
    @Expose
    private String paidCurrency;
    @SerializedName("PaidCurrencyValue")
    @Expose
    private String paidCurrencyValue;
    @SerializedName("Currency")
    @Expose
    private String currency;
    @SerializedName("Error")
    @Expose
    private Object error;
    @SerializedName("CardNumber")
    @Expose
    private String cardNumber;

    public Integer getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Integer transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(String authorizationId) {
        this.authorizationId = authorizationId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransationValue() {
        return transationValue;
    }

    public void setTransationValue(String transationValue) {
        this.transationValue = transationValue;
    }

    public String getCustomerServiceCharge() {
        return customerServiceCharge;
    }

    public void setCustomerServiceCharge(String customerServiceCharge) {
        this.customerServiceCharge = customerServiceCharge;
    }

    public String getDueValue() {
        return dueValue;
    }

    public void setDueValue(String dueValue) {
        this.dueValue = dueValue;
    }

    public String getPaidCurrency() {
        return paidCurrency;
    }

    public void setPaidCurrency(String paidCurrency) {
        this.paidCurrency = paidCurrency;
    }

    public String getPaidCurrencyValue() {
        return paidCurrencyValue;
    }

    public void setPaidCurrencyValue(String paidCurrencyValue) {
        this.paidCurrencyValue = paidCurrencyValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

}
