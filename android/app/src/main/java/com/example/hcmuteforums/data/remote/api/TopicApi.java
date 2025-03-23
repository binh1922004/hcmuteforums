package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TopicApi {
    @GET("api/topics")
    Call<ApiResponse<List<TopicDetailResponse>>> getAllTopic();
}
