package com.example.hcmuteforums.data.remote.api;


import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.FollowerResponse;
import com.example.hcmuteforums.model.dto.response.FollowingResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchApi {
    @GET("api/search")
    Call<ApiResponse<PageResponse<TopicDetailResponse>>> searchTopic(@Query("keyword") String keyword, @Query("page") int page);
    @GET("api/search/follower")
    Call<ApiResponse<PageResponse<FollowerResponse>>> getFollowerByUsername(@Query("username") String username, @Query("targetUsername") String targetUsername, @Query("page") int page);
    @GET("api/search/following")
    Call<ApiResponse<PageResponse<FollowingResponse>>> getFollowingByUsername(@Query("username") String username, @Query("targetUsername") String targetUsername, @Query("page") int page);
}
