package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.TopicPostRequest;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TopicApi {
    @GET("api/topics")
    Call<ApiResponse<List<TopicDetailResponse>>> getAllTopic();

    @POST("api/post")
    Call<ApiResponse<Boolean>> postTopic(@Body TopicPostRequest topicPostRequest);
}
