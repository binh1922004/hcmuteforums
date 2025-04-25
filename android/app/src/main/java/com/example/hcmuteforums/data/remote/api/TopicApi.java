package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.request.TopicPostRequest;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TopicApi {
    @GET("api/topics")
    Call<ApiResponse<PageResponse<TopicDetailResponse>>> getAllTopic(@Query("page") int page);

    @POST("api/topics/post")
    Call<ApiResponse<TopicDetailResponse>> postTopic(@Body TopicPostRequest topicPostRequest);

    @Multipart
    @POST("api/topic-images/{topicId}/upload")
    Call<ApiResponse<Boolean>> uploadImages(@Path("topicId") String id, @Part List<MultipartBody.Part> images);
}
