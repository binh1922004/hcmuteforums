package com.example.hcmuteforums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.UserApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.UserCreationRequest;
import com.example.hcmuteforums.model.dto.response.UserResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    public static UserRepository instance;
    private MutableLiveData<Boolean> registerResponse = new MutableLiveData<>();
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    private MutableLiveData<Boolean> regiserError = new MutableLiveData<>();

    private MutableLiveData<UserResponse> userInfo = new MutableLiveData<>();

    private MutableLiveData<Boolean> userInfoError = new MutableLiveData<>();

    private UserApi userApi;

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    public UserRepository(){
        //api
        userApi = LocalRetrofit.getRetrofit().create(UserApi.class);
        //data
    }
    public void getInfo(){
        userApi.myInfo().enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<UserResponse> apiRes = response.body();
                    if (apiRes.getResult()!=null){
                        userInfo.setValue(apiRes.getResult());
                    }
                    else{
                        userInfoError.setValue(true);
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
                        userInfoError.setValue(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable throwable) {
                Log.d("Error UserInfo", throwable.getMessage());
                userInfoError.setValue(true);
            }
        });
    }

    public MutableLiveData<UserResponse> getUserInfo() {
        return userInfo;
    }

    public void register(UserCreationRequest userCreationRequest){
        userApi.register(userCreationRequest).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<Boolean> apiRes = response.body();
                    if (apiRes.getResult()){
                        registerResponse.setValue(true);
                    }
                    else{
                        regiserError.setValue(true);
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
                        regiserError.setValue(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable throwable) {
                Log.d("Error Regiser", throwable.getMessage());
                regiserError.setValue(true);
            }
        });
    }

    public MutableLiveData<Boolean> getRegisterResponse() {
        return registerResponse;
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Boolean> getRegisterError() {
        return regiserError;
    }

    public MutableLiveData<Boolean> getUserInfoError() {
        return userInfoError;
    }
}
