package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.PasswordUpdateRequest;
import com.example.hcmuteforums.model.dto.request.UserCreationRequest;
import com.example.hcmuteforums.model.dto.request.UserUpdateRequest;
import com.example.hcmuteforums.model.dto.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserApi {
    @POST("api/users")
    public Call<ApiResponse<Boolean>> register(@Body UserCreationRequest userCreationRequest);
    @GET("api/users/myInfo")
    public Call<ApiResponse<UserResponse>> myInfo();
    @GET("api/users/personInfo")
    public Call<ApiResponse<UserResponse>> personInfo(@Query("username") String username);
    @PUT("api/users/update")
    public Call<ApiResponse<UserResponse>> updateUser(@Body UserUpdateRequest userUpdateRequest);
    @PUT("api/users/updatePassword")
    public Call<ApiResponse<Boolean>> updatePassword(@Body PasswordUpdateRequest passwordUpdateRequest);
}
