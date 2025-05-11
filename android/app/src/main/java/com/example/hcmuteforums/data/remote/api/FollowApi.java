package com.example.hcmuteforums.data.remote.api;

import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.models.selection.PageSelection;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.request.FollowRequest;
import com.example.hcmuteforums.model.dto.response.FollowResponse;
import com.example.hcmuteforums.model.dto.response.FollowStatusResponse;
import com.example.hcmuteforums.model.dto.response.FollowerResponse;
import com.example.hcmuteforums.model.dto.response.FollowingResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FollowApi {
    @POST("api/follow")
    Call<ApiResponse<FollowResponse>> followUser(@Body FollowRequest followRequest);
    @DELETE("api/follow/unfollow")
    Call<ApiResponse<FollowResponse>> unfollowUser(@Query("targetUsername")  String targetUsername);
    @GET("api/follow/followers")
    Call<ApiResponse<PageResponse<FollowerResponse>>> getFollowers(
            @Query("username") String username,
            @Query("page") int page
    );

    @GET("api/follow/following")
    Call<ApiResponse<PageResponse<FollowingResponse>>> getFollowings(
            @Query("username") String username,
            @Query("page") int page
    );
    @GET("api/follow/check")
    Call<ApiResponse<FollowStatusResponse>> checkFollowStatus(
            @Query("currentUsername") String currentUsername,
            @Query("targetUsername") String targetUsername
    );
}
