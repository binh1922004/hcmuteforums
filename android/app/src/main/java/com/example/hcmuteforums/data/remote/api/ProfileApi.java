package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.ProfileUpdateRequest;
import com.example.hcmuteforums.model.dto.response.ProfileResponse;

import java.util.function.BiPredicate;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ProfileApi {
    @GET("api/profiles/myProfile")
    public Call<ApiResponse<ProfileResponse>> myProfile();

    @GET("api/profiles/personProfile")
    public Call<ApiResponse<ProfileResponse>> personProfile(@Query("username") String username);
    @PUT("api/profiles/updateProfile")
    public Call<ApiResponse<ProfileResponse>> updateProfile(@Body ProfileUpdateRequest profileUpdateRequest);

    @Multipart
    @POST("api/profiles/upload-cover")
    public Call<ApiResponse<Boolean>> uploadCoverImage(@Part MultipartBody.Part file);

    @Multipart
    @POST("api/profiles/upload-avatar")
    public Call<ApiResponse<Boolean>> uploadAvatarImage(@Part MultipartBody.Part file);
}
