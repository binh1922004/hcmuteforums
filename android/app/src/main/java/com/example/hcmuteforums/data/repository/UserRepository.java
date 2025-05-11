package com.example.hcmuteforums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.UserApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.PasswordUpdateRequest;
import com.example.hcmuteforums.model.dto.request.UserCreationRequest;
import com.example.hcmuteforums.model.dto.request.UserUpdateRequest;
import com.example.hcmuteforums.model.dto.response.UserResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    public static UserRepository instance;

    private final MutableLiveData<Event<Boolean>> registerResponse = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> registerError = new MutableLiveData<>();

    private final MutableLiveData<UserResponse> userInfo = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> userInfoError = new MutableLiveData<>();

    private final MutableLiveData<UserResponse> personInfo = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> personInfoError = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> userUpdateError = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> updateResponse = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> updatePasswordResponse = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> updatePasswordError = new MutableLiveData<>();


    private final UserApi userApi;

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    public UserRepository() {
        userApi = LocalRetrofit.getRetrofit().create(UserApi.class);
    }

    public void getInfo(Callback<ApiResponse<UserResponse>> callback) {
        var call = userApi.myInfo();
        call.enqueue(callback);
    }
    public void getInfoPerson(String username, Callback<ApiResponse<UserResponse>> callback){
        var call = userApi.personInfo(username);
        call.enqueue(callback);
    }

    public void register(UserCreationRequest userCreationRequest, Callback<ApiResponse<Boolean>> callback) {
        var call = userApi.register(userCreationRequest);
        call.enqueue(callback);
    }

    public void updateUser(UserUpdateRequest userUpdateRequest, Callback<ApiResponse<UserResponse>> callback) {
        var call = userApi.updateUser(userUpdateRequest);
        call.enqueue(callback);
    }
    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest, Callback<ApiResponse<Boolean>> callback){
        var call = userApi.updatePassword(passwordUpdateRequest);
        call.enqueue(callback);
    }

    public MutableLiveData<UserResponse> getPersonInfo() {
        return personInfo;
    }

    public MutableLiveData<Event<Boolean>> getPersonInfoError() {
        return personInfoError;
    }
    // Getters

    public MutableLiveData<Event<Boolean>> getRegisterResponse() {
        return registerResponse;
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Event<Boolean>> getRegisterError() {
        return registerError;
    }

    public MutableLiveData<UserResponse> getUserInfo() {
        return userInfo;
    }

    public MutableLiveData<Event<Boolean>> getUserInfoError() {
        return userInfoError;
    }

    public MutableLiveData<Event<Boolean>> getUserUpdateError() {
        return userUpdateError;
    }

    public MutableLiveData<Event<Boolean>> getUpdateResponse() {
        return updateResponse;
    }

    public MutableLiveData<Event<Boolean>> getUpdatePasswordResponse() {
        return updatePasswordResponse;
    }

    public MutableLiveData<Event<Boolean>> getUpdatePasswordError() {
        return updatePasswordError;
    }
}

