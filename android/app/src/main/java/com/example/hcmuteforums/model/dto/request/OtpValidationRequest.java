package com.example.hcmuteforums.model.dto.request;

public class OtpValidationRequest {
    private String email;
    private String otp;

    public OtpValidationRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
