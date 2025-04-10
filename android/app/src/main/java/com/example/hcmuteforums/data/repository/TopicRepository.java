package com.example.hcmuteforums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.TopicApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.event.Event;
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
    private MutableLiveData<Event<Boolean>> topicError = new MutableLiveData<>();
    private MutableLiveData<Event<String>> messageError = new MutableLiveData<>();

    private TopicApi topicApi;

    public static TopicRepository getInstance() {
        if (instance == null)
            instance = new TopicRepository();
        return instance;
    }

    public TopicRepository() {
        topicApi = LocalRetrofit.getRetrofit().create(TopicApi.class);
    }

    public void getAllTopics(Callback<ApiResponse<List<TopicDetailResponse>>> callback) {
        var call = topicApi.getAllTopic();
        call.enqueue(callback);
    }

    public MutableLiveData<List<TopicDetailResponse>> getTopicsLiveData() {
        return topicsLiveData;
    }

    public MutableLiveData<Event<Boolean>> getTopicError() {
        return topicError;
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }
}
