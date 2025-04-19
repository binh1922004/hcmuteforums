package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
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

public interface LikeApi {
    @POST("api/like")
    Call<ApiResponse<Boolean>> likeTopic(@Query("topicId") String topicId);
}
