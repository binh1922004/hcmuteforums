package com.example.hcmuteforums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.AuthenticationApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.AuthenticationRequest;
import com.example.hcmuteforums.model.dto.response.AuthenticationResponse;
import com.example.hcmuteforums.utils.ApiErrorHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthenticationRepository {
    public static AuthenticationRepository instance;
    private MutableLiveData<ApiResponse<AuthenticationResponse>> loginResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private AuthenticationApi authenticationApi;
    public AuthenticationRepository(){
        LocalRetrofit localRetrofit = new LocalRetrofit();
        authenticationApi = localRetrofit.getRetrofit().create(AuthenticationApi.class);
    }
    public static AuthenticationRepository getInstance() {
        if (instance == null)
            instance = new AuthenticationRepository();
        return instance;
    }

    public void login(String username, String password){
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);
        authenticationApi.login(authenticationRequest).enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, Response<ApiResponse<AuthenticationResponse>> response) {
                ApiResponse<?> apiResp;
                if (response.isSuccessful() && response.body() != null){
                    apiResp = response.body();
                }
                else{
                    apiResp = ApiErrorHandler.parseError(response);
                }

                if (apiResp.getCode() == 200){
                    loginResponse.setValue((ApiResponse<AuthenticationResponse>) apiResp);
                }
                else{
                    loginError.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable throwable) {
                Log.d("Loi", throwable.getMessage());
                loginError.setValue(true);
            }
        });
    }

    public MutableLiveData<ApiResponse<AuthenticationResponse>> getLoginResponse() {
        return loginResponse;
    }

    public MutableLiveData<Boolean> getLoginError() {
        return loginError;
    }


}
