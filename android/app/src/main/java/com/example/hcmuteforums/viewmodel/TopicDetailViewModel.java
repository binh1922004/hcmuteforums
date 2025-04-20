package com.example.hcmuteforums.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.TopicRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicDetailViewModel extends ViewModel {
    private TopicRepository topicRepository;

    private MutableLiveData<Event<Boolean>> likeTopicSuccess = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> likeTopicError = new MutableLiveData<>();
    private MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    public TopicDetailViewModel(){
        topicRepository = TopicRepository.getInstance();
    }
    public void likeTopic(String topicId) {
        topicRepository.likeTopic(topicId, new Callback<ApiResponse<Boolean>>() {
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Boolean> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        likeTopicSuccess.setValue(new Event<>(apiRes.getResult()));  // ✅ Không dùng Event
                    } else {
                        likeTopicError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        likeTopicError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable throwable) {
                Log.d("Error Topic", throwable.getMessage());
                likeTopicError.setValue(new Event<>(true));
            }
        });
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Event<Boolean>> getLikeTopicSuccess() {
        return likeTopicSuccess;
    }

    public MutableLiveData<Event<Boolean>> getLikeTopicError() {
        return likeTopicError;
    }
}
