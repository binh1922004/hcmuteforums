package com.example.hcmuteforums.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.UserRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.PasswordUpdateRequest;
import com.example.hcmuteforums.model.dto.request.UserCreationRequest;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPViewModel extends ViewModel {
    private UserRepository userRepository;
    private final MutableLiveData<Event<Boolean>> registerResponse = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> registerError = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> updatePasswordResponse = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> updatePasswordError = new MutableLiveData<>();
    public VerifyOTPViewModel(){
        userRepository = UserRepository.getInstance();
    }

    public void register(UserCreationRequest userCreationRequest){
        userRepository.register(userCreationRequest, new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Boolean> apiRes = response.body();
                    if (apiRes.getResult()) {
                        registerResponse.setValue(new Event<>(true));
                    } else {
                        registerError.setValue(new Event<>(true));
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));
                    } else {
                        registerError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable throwable) {
                Log.d("Error Register", throwable.getMessage());
                registerError.setValue(new Event<>(true));
            }
        });
    }
    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest){
        userRepository.updatePassword(passwordUpdateRequest, new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    ApiResponse<Boolean> apiRes = response.body();
                    if(apiRes.getResult()){
                        updatePasswordResponse.setValue(new Event<>(true));
                    }else{
                        updatePasswordError.setValue(new Event<>(true));
                    }
                }else{
                    if(response.errorBody()!=null){
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));
                    }else{
                        updatePasswordError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable throwable) {
                Log.d("Error Update Password", throwable.getMessage());
                updatePasswordError.setValue(new Event<>(true));
            }
        });
    }

    public MutableLiveData<Event<Boolean>> getRegisterResponse() {
        return registerResponse;
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Event<Boolean>> getRegisterError() {
        return registerError;
    }

    public MutableLiveData<Event<Boolean>> getUpdatePasswordResponse() {
        return updatePasswordResponse;
    }

    public MutableLiveData<Event<Boolean>> getUpdatePasswordError() {
        return updatePasswordError;
    }
}
