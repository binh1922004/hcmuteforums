package com.example.hcmuteforums.data.remote.api;


import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchApi {
    @GET("api/search")
    Call<ApiResponse<PageResponse<TopicDetailResponse>>> searchTopic(@Query("keyword") String keyword, @Query("page") int page);
}
