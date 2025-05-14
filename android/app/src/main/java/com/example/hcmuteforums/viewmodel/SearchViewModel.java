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
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {
    private TopicRepository topicRepository;

    public SearchViewModel(){
        topicRepository = TopicRepository.getInstance();
    }

    private MutableLiveData<PageResponse<TopicDetailResponse>> searchTopicLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> searchTopicError = new MutableLiveData<>();
    private MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<PageResponse<TopicDetailResponse>> getSearchTopicLiveData() {
        return searchTopicLiveData;
    }

    public MutableLiveData<Event<Boolean>> getSearchTopicError() {
        return searchTopicError;
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void searchUser(String username, int page){

    }
    public void searchTopic(String keyword, int page){
        isLoading.setValue(true);
        topicRepository.searchTopics(keyword, page, new Callback<ApiResponse<PageResponse<TopicDetailResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageResponse<TopicDetailResponse>>> call, Response<ApiResponse<PageResponse<TopicDetailResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageResponse<TopicDetailResponse>> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        searchTopicLiveData.setValue(apiRes.getResult());  // ✅ Không dùng Event
                    } else {
                        searchTopicError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        searchTopicError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageResponse<TopicDetailResponse>>> call, Throwable throwable) {
                Log.d("ErrorSearchViewModel", throwable.getMessage());
                searchTopicError.setValue(new Event<>(true));
            }
        });
    }

}
