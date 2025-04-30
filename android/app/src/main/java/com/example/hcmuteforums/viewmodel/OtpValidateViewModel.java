package com.example.hcmuteforums.viewmodel;

import android.util.Log;

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

public class OtpValidateViewModel extends ViewModel {
    private OtpRepository otpRepository;
    private final MutableLiveData<Event<Boolean>> otpValidatedResponse = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> messageError = new MutableLiveData<>();

    private final MutableLiveData<Event<Boolean>> otpValidatedError = new MutableLiveData<>();
    public OtpValidateViewModel() {
        this.otpRepository = new OtpRepository();
    }
    public void validateOtp(String email, String otp){
        otpRepository.validateOtp(email, otp, new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Boolean> apiRes = response.body();
                    if (apiRes.getResult()) {
                        otpValidatedResponse.setValue(new Event<>(true));
                    } else {
                        otpValidatedError.setValue(new Event<>(true));
                    }
                }else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));
                    } else {
                        otpValidatedError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable throwable) {
                Log.d("Error Validation", throwable.getMessage());
                otpValidatedError.setValue(new Event<>(true));
            }
        });
    }

    public MutableLiveData<Event<Boolean>> getOtpValidatedResponse() {
        return otpValidatedResponse;
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Event<Boolean>> getOtpValidatedError() {
        return otpValidatedError;
    }
}
