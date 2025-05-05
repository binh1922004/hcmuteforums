package com.example.hcmuteforums.model.dto.request;

import java.io.Serializable;

public class PasswordUpdateRequest implements Serializable {
    private String email;
    private String password;
    private String otp;
    private String username;


    public PasswordUpdateRequest(String email, String password, String otp, String username) {
        this.email = email;
        this.password = password;
        this.otp = otp;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
