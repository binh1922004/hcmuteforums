package com.example.hcmuteforums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.OtpApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.OtpRequest;
import com.example.hcmuteforums.model.dto.request.OtpValidationRequest;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpRepository {
    private static OtpRepository instance;
    private OtpApi otpApi;
    public static OtpRepository getInstance() {
        if (instance == null)
            instance = new OtpRepository();
        return instance;
    }

    public OtpRepository() {
        otpApi = LocalRetrofit.getRetrofit().create(OtpApi.class);
    }


    public void sendOtp(String email, String username, Callback<ApiResponse<Boolean>> callback) {
        OtpRequest otpRequest = new OtpRequest(email, username);
        var call = otpApi.sendOtp(otpRequest);
        call.enqueue(callback);
    }
    public void sendOtpResetPasword(String email, String username, Callback<ApiResponse<Boolean>> callback){
        OtpRequest otpRequest = new OtpRequest(email, username);
        var call = otpApi.sendOtpResetPasword(otpRequest);
        call.enqueue(callback);
    }
    public void validateOtp(String email, String otp, Callback<ApiResponse<Boolean>> callback){
        OtpValidationRequest otpValidationRequest = new OtpValidationRequest(email, otp);
        var call = otpApi.validateOTP(otpValidationRequest);
        call.enqueue(callback);
    }
}
