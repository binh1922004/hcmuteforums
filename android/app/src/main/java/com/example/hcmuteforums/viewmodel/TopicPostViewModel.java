package com.example.hcmuteforums.viewmodel;

import android.content.Context;
import android.net.Uri;
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

    private MutableLiveData<Event<TopicDetailResponse>> topicPostSuccess = new MutableLiveData<>();
    private MutableLiveData<TopicDetailResponse> imageUploadSuccess = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> imageUploadError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> topicPostError = new MutableLiveData<>();
    private MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    public TopicPostViewModel() {
        topicRepository = TopicRepository.getInstance();
    }

    public void postTopic(String title, String content){
        isLoading.setValue(true);
        topicRepository.postTopic(title, content, new Callback<ApiResponse<TopicDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<TopicDetailResponse>> call, Response<ApiResponse<TopicDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<TopicDetailResponse> apiRes = response.body();
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
            public void onFailure(Call<ApiResponse<TopicDetailResponse>> call, Throwable throwable) {
                Log.d("Error post", throwable.getMessage());
                topicPostError.setValue(new Event<>(true));
            }
        });
    }
    public void uploadImage(String topicId, List<Uri> images, Context context){
        topicRepository.uploadImage(topicId, images, context, new Callback<ApiResponse<TopicDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<TopicDetailResponse>> call, Response<ApiResponse<TopicDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<TopicDetailResponse> apiRes = response.body();
                    Log.d("ErrorOfUpload", apiRes.getResult().getId());
                    if (apiRes.getResult() != null) {
                        imageUploadSuccess.setValue(apiRes.getResult());  // ✅ Không dùng Event
                    } else {
                        imageUploadError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        imageUploadError.setValue(new Event<>(true));
                    }
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<ApiResponse<TopicDetailResponse>> call, Throwable throwable) {
                Log.d("Error post", throwable.getMessage());
                isLoading.setValue(false);
                imageUploadError.setValue(new Event<>(true));
            }
        });
    }


    public MutableLiveData<Event<TopicDetailResponse>> getTopicPostSuccess() {
        return topicPostSuccess;
    }

    public MutableLiveData<Event<Boolean>> getTopicPostError() {
        return topicPostError;
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }

    public MutableLiveData<TopicDetailResponse> getImageUploadSuccess() {
        return imageUploadSuccess;
    }

    public MutableLiveData<Event<Boolean>> getImageUploadError() {
        return imageUploadError;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
