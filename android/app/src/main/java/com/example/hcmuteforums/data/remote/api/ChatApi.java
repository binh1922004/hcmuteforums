package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.ChatMessage;
import com.example.hcmuteforums.model.dto.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChatApi {
    @POST("api/chatbot/ask")
    Call<ApiResponse<String>> askChatBot(@Query("request") String message);
}