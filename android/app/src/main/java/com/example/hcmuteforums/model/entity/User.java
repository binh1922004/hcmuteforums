package com.example.hcmuteforums.model.entity;

public class User {
    private String username;
    private String password;
    private String email;
    private String dob;
    private String fullname;
    private String gender;
    private String mssv;
    private String phone;
    private String address;

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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User(String username, String password, String email, String dob, String fullname, String gender, String mssv, String phone, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dob = dob;
        this.fullname = fullname;
        this.gender = gender;
        this.mssv = mssv;
        this.phone = phone;
        this.address = address;
    }
}
