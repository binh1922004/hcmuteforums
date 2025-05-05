package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.OtpRequest;
import com.example.hcmuteforums.model.dto.request.OtpValidationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface OtpApi {
    @POST("api/otp/get")
    public Call<ApiResponse<Boolean>> sendOtp(@Body OtpRequest otpRequest);
    @POST("api/otp/getOTPResetPassword")
    public Call<ApiResponse<Boolean>> sendOtpResetPasword(@Body OtpRequest otpRequest);
    @POST("api/otp/validatedOTP")
    public Call<ApiResponse<Boolean>> validateOTP(@Body OtpValidationRequest otpValidationRequest);
}
