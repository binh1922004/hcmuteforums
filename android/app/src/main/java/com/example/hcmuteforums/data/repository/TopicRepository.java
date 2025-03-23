package com.example.hcmuteforums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.TopicApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicRepository {
    private static TopicRepository instance;
    private MutableLiveData<List<TopicDetailResponse>> topicsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> topicError = new MutableLiveData<>();
    private MutableLiveData<String> messageError = new MutableLiveData<>();

    private TopicApi topicApi;

    public static TopicRepository getInstance() {
        if (instance == null)
            instance = new TopicRepository();
        return instance;
    }

    public TopicRepository() {
        topicApi = LocalRetrofit.getRetrofit().create(TopicApi.class);
    }

    public void getAllTopics() {
        topicApi.getAllTopic().enqueue(new Callback<ApiResponse<List<TopicDetailResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<TopicDetailResponse>>> call, Response<ApiResponse<List<TopicDetailResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<TopicDetailResponse>> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        topicsLiveData.setValue(apiRes.getResult());
                    } else {
                        topicError.setValue(true);
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(apiError.getMessage());
                    } else {
                        topicError.setValue(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TopicDetailResponse>>> call, Throwable throwable) {
                Log.d("Error Topic", throwable.getMessage());
                topicError.setValue(true);
            }
        });
    }

    public MutableLiveData<List<TopicDetailResponse>> getTopicsLiveData() {
        return topicsLiveData;
    }

    public MutableLiveData<Boolean> getTopicError() {
        return topicError;
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }
}
