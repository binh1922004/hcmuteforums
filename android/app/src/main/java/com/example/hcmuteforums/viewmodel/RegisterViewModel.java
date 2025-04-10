package com.example.hcmuteforums.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.OtpRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {
    private OtpRepository otpRepository;
    private MutableLiveData<Event<Boolean>> sendOtpError;
    private MutableLiveData<Event<Boolean>> sendOtpResponse;
    private MutableLiveData<Event<String>> messageError;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public RegisterViewModel(){
        otpRepository = OtpRepository.getInstance();
    }

    public void sendOtp(String email, String username){
        isLoading.setValue(true);
        otpRepository.sendOtp(email, username, new Callback<ApiResponse<Boolean>>() {
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
}
