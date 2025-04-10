package com.example.hcmuteforums.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.TopicRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicViewModel extends ViewModel {
    private TopicRepository topicRepository;

    private MutableLiveData<List<TopicDetailResponse>> topicsLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> topicError = new MutableLiveData<>();
    private MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    public TopicViewModel() {
        topicRepository = TopicRepository.getInstance();
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

    public void fetchAllTopics() {
        topicRepository.getAllTopics(new Callback<ApiResponse<List<TopicDetailResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<TopicDetailResponse>>> call, Response<ApiResponse<List<TopicDetailResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<TopicDetailResponse>> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        topicsLiveData.setValue(apiRes.getResult());  // ✅ Không dùng Event
                    } else {
                        topicError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        topicError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TopicDetailResponse>>> call, Throwable throwable) {
                Log.d("Error Topic", throwable.getMessage());
                topicError.setValue(new Event<>(true));
            }
        });
    }
}
