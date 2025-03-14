package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.UserCreationRequest;
import com.example.hcmuteforums.model.dto.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {
    @POST("api/users")
    public Call<ApiResponse<Boolean>> register(@Body UserCreationRequest userCreationRequest);
    @GET("api/users/myInfo")
    public Call<ApiResponse<UserResponse>> myInfo();
}
