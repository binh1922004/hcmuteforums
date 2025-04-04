package com.example.hcmuteforums.model.dto.request;

import java.io.Serializable;
import java.util.Date;

public class UserUpdateRequest implements Serializable {
    String fullName;
    String phone;
    Date dob;
    String address;
    String gender;

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(String fullName, String phone, Date dob, String address, String gender) {
        this.fullName = fullName;
        this.phone = phone;
        this.dob = dob;
        this.address = address;
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

