package com.example.hcmuteforums.data.repository;

import com.example.hcmuteforums.data.remote.api.NotificationApi;
import com.example.hcmuteforums.data.remote.api.ReplyApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.NotificationDTO;
import com.example.hcmuteforums.model.dto.PageResponse;

import retrofit2.Callback;

public class NotificationRepository {
    private static NotificationRepository instance;
    private NotificationApi notiApi;

    public static NotificationRepository getInstance() {
        if (instance == null)
            instance = new NotificationRepository();
        return instance;
    }

    public NotificationRepository() {
        notiApi = LocalRetrofit.getRetrofit().create(NotificationApi.class);
    }

    public void getAllNotifications(int page, Callback<ApiResponse<PageResponse<NotificationDTO>>> callback){
        var call = notiApi.getAllNotifications(page);
        call.enqueue(callback);
    }

}
