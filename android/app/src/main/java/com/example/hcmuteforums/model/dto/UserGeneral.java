package com.example.hcmuteforums.model.dto;

import java.io.Serializable;

public class UserGeneral implements Serializable {
    private String fullName;
    private String username;
    private String avt;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }
}
