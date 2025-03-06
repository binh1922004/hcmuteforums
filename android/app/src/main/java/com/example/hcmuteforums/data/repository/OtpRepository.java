package com.example.hcmuteforums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.OtpApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.OtpRequest;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpRepository {
    private static OtpRepository instance;
    private OtpApi otpApi;
    private MutableLiveData<Boolean> sendOtpError;
    private MutableLiveData<Boolean> sendOtpResponse;
    private MutableLiveData<String> messageError;

    public static OtpRepository getInstance(){
        if (instance == null)
            instance = new OtpRepository();
        return instance;
    }
    public OtpRepository(){
        LocalRetrofit localRetrofit = new LocalRetrofit();
        otpApi = localRetrofit.getRetrofit().create(OtpApi.class);
        sendOtpError = new MutableLiveData<>();
        sendOtpResponse = new MutableLiveData<>();
        messageError = new MutableLiveData<>();
    }
    public void sendOtp(String email, String username){
        OtpRequest otpRequest = new OtpRequest(email, username);
        otpApi.sendOtp(otpRequest).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<Boolean> apiRes = response.body();
                    if (apiRes.getResult()){
                        sendOtpResponse.setValue(true);
                    }
                    else{
                        sendOtpError.setValue(true);
                    }
                }
                else{
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(apiError.getMessage());
                    }
                    else {
                        sendOtpError.setValue(true);
                    }

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable throwable) {
                Log.d("Otp Error", "Can not send OTP");
                sendOtpError.setValue(true);
            }
        });
    }

    public MutableLiveData<Boolean> getSendOtpError() {
        return sendOtpError;
    }

    public MutableLiveData<Boolean> getSendOtpResponse() {
        return sendOtpResponse;
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }
}
