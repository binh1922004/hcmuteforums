package com.example.hcmuteforums.utils;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.google.gson.Gson;

import retrofit2.Response;


public class ApiErrorHandler {
    public static ApiResponse<?> parseError(Response<?> response) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(response.errorBody().string(), ApiResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(500, null, "Lỗi không xác định");
        }
    }
}