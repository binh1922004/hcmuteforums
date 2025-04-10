package com.example.hcmuteforums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.AuthenticationApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.AuthenticationRequest;
import com.example.hcmuteforums.model.dto.response.AuthenticationResponse;
import com.example.hcmuteforums.utils.ApiErrorHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthenticationRepository {
    public static AuthenticationRepository instance;
    private AuthenticationApi authenticationApi;
    public AuthenticationRepository(){
        authenticationApi = LocalRetrofit.getRetrofit().create(AuthenticationApi.class);
    }
    public static AuthenticationRepository getInstance() {
        if (instance == null)
            instance = new AuthenticationRepository();
        return instance;
    }

    public void login(String username, String password, Callback<ApiResponse<AuthenticationResponse>> callback) {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);
        Call<ApiResponse<AuthenticationResponse>> call = authenticationApi.login(authenticationRequest);
        call.enqueue(callback);
    }
}
