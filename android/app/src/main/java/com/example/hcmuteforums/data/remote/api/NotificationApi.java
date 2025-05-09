package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.NotificationDTO;
import com.example.hcmuteforums.model.dto.PageResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationApi {
    @GET("api/notification")
    Call<ApiResponse<PageResponse<NotificationDTO>>> getAllNotifications(@Query("page") int page);
}
