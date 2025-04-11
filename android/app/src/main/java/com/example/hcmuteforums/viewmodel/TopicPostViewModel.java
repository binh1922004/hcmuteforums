package com.example.hcmuteforums.viewmodel;

import android.util.Log;

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

public class TopicPostViewModel extends ViewModel {
    private TopicRepository topicRepository;

    private MutableLiveData<Event<Boolean>> topicPostSuccess = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> topicPostError = new MutableLiveData<>();
    private MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    public TopicPostViewModel() {
        topicRepository = TopicRepository.getInstance();
    }

    public void postTopic(String title, String content, String categoryId){
        topicRepository.postTopic(title, content, categoryId, new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Boolean> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        topicPostSuccess.setValue(new Event<>(apiRes.getResult()));  // ✅ Không dùng Event
                    } else {
                        topicPostError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        topicPostError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable throwable) {
                Log.d("Error post", throwable.getMessage());
                topicPostError.setValue(new Event<>(true));
            }
        });
    }


    public MutableLiveData<Event<Boolean>> getTopicPostSuccess() {
        return topicPostSuccess;
    }

    public MutableLiveData<Event<Boolean>> getTopicPostError() {
        return topicPostError;
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }
}
