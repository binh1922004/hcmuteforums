package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.AuthenticationRequest;
import com.example.hcmuteforums.model.dto.response.AuthenticationResponse;
import com.example.hcmuteforums.model.dto.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthenticationApi {
    @POST("api/auth/login")
    public Call<ApiResponse<AuthenticationResponse>> login(@Body AuthenticationRequest authenticationRequest);

    @GET("api/users/myInfo")
    public Call<ApiResponse<UserResponse>> myInfo();

    @POST("api/auth/introspect")
    public Call<ApiResponse<Boolean>> introspect(@Query("token") String token);
}
