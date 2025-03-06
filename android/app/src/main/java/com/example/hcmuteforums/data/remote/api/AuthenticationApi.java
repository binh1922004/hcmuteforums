package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.AuthenticationRequest;
import com.example.hcmuteforums.model.dto.response.AuthenticationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationApi {
    @POST("api/auth/login")
    public Call<ApiResponse<AuthenticationResponse>> login(@Body AuthenticationRequest authenticationRequest);

}
