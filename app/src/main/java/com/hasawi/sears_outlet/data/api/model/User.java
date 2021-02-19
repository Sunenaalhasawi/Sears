package com.hasawi.sears_outlet.data.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("customerFirstName")
    @Expose
    private String customerFirstName;
    @SerializedName("customerLastName")
    @Expose
    private String customerLastName;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("confirmPassword")
    @Expose
    private String confirmPassword;
    @SerializedName("active")
    @Expose
    private boolean active;
    @SerializedName("emailId")
    @Expose
    private String emailId;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("nationality")
    @Expose
    private String nationality;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
