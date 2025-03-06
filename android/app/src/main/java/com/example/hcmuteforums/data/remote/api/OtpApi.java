package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.OtpRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OtpApi {
    @POST("api/otp/get")
    public Call<ApiResponse<Boolean>> sendOtp(@Body OtpRequest otpRequest);
}
