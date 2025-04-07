package com.example.hcmuteforums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.OtpApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.event.Event;
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

    private MutableLiveData<Event<Boolean>> sendOtpError;
    private MutableLiveData<Event<Boolean>> sendOtpResponse;
    private MutableLiveData<Event<String>> messageError;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public static OtpRepository getInstance() {
        if (instance == null)
            instance = new OtpRepository();
        return instance;
    }

    public OtpRepository() {
        otpApi = LocalRetrofit.getRetrofit().create(OtpApi.class);
        sendOtpError = new MutableLiveData<>();
        sendOtpResponse = new MutableLiveData<>();
        messageError = new MutableLiveData<>();
    }

    public MutableLiveData<Event<Boolean>> getSendOtpError() {
        return sendOtpError;
    }

    public MutableLiveData<Event<Boolean>> getSendOtpResponse() {
        return sendOtpResponse;
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void sendOtp(String email, String username) {
        isLoading.setValue(true);
        OtpRequest otpRequest = new OtpRequest(email, username);
        otpApi.sendOtp(otpRequest).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Boolean> apiRes = response.body();
                    if (apiRes.getResult()) {
                        sendOtpResponse.setValue(new Event<>(true));
                    } else {
                        sendOtpError.setValue(new Event<>(true));
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));
                    } else {
                        sendOtpError.setValue(new Event<>(true));
                    }
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable throwable) {
                Log.d("Otp Error", "Can not send OTP: " + throwable.getMessage());
                sendOtpError.setValue(new Event<>(true));
                isLoading.setValue(false);
            }
        });
    }
}
