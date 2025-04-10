package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.ProfileUpdateRequest;
import com.example.hcmuteforums.model.dto.response.ProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface ProfileApi {
    @GET("api/profiles/myProfile")
    public Call<ApiResponse<ProfileResponse>> myProfile();
    @PUT("api/profiles/updateProfile")
    public Call<ApiResponse<ProfileResponse>> updateProfile(@Body ProfileUpdateRequest profileUpdateRequest);
}
