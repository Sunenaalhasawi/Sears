package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Invoice {

    @SerializedName("InvoiceId")
    @Expose
    private Integer invoiceId;
    @SerializedName("InvoiceStatus")
    @Expose
    private String invoiceStatus;
    @SerializedName("InvoiceReference")
    @Expose
    private String invoiceReference;
    @SerializedName("CustomerReference")
    @Expose
    private String customerReference;
    @SerializedName("CreatedDate")
    @Expose
    private Object createdDate;
    @SerializedName("ExpiryDate")
    @Expose
    private String expiryDate;
    @SerializedName("InvoiceValue")
    @Expose
    private Integer invoiceValue;
    @SerializedName("Comments")
    @Expose
    private String comments;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("CustomerMobile")
    @Expose
    private String customerMobile;
    @SerializedName("CustomerEmail")
    @Expose
    private String customerEmail;
    @SerializedName("UserDefinedField")
    @Expose
    private String userDefinedField;
    @SerializedName("InvoiceDisplayValue")
    @Expose
    private String invoiceDisplayValue;
    @SerializedName("InvoiceItems")
    @Expose
    private List<Object> invoiceItems = null;
    @SerializedName("InvoiceTransactions")
    @Expose
    private List<InvoiceTransaction> invoiceTransactions = null;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getInvoiceReference() {
        return invoiceReference;
    }

    public void setInvoiceReference(String invoiceReference) {
        this.invoiceReference = invoiceReference;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public Object getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(Integer invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getUserDefinedField() {
        return userDefinedField;
    }

    public void setUserDefinedField(String userDefinedField) {
        this.userDefinedField = userDefinedField;
    }

    public String getInvoiceDisplayValue() {
        return invoiceDisplayValue;
    }

    public void setInvoiceDisplayValue(String invoiceDisplayValue) {
        this.invoiceDisplayValue = invoiceDisplayValue;
    }

    public List<Object> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<Object> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public List<InvoiceTransaction> getInvoiceTransactions() {
        return invoiceTransactions;
    }

    public void setInvoiceTransactions(List<InvoiceTransaction> invoiceTransactions) {
        this.invoiceTransactions = invoiceTransactions;
    }

}
