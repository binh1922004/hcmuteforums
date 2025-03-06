package com.example.hcmuteforums.model.dto.request;

import java.util.Date;

public class UserCreationRequest {
    String username;
    String password;
    String email;
    String fullName;
    Date dob;
    String otp;

    public UserCreationRequest(String username, String password, String email, String fullName, Date dob, String otp) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.dob = dob;
        this.otp = otp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
