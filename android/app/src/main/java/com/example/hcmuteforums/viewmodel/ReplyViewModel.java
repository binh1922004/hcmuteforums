package com.example.hcmuteforums.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.ReplyRepository;
import com.example.hcmuteforums.data.repository.TopicRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReplyViewModel extends ViewModel {
    private ReplyRepository replyRepository;

    private MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    private MutableLiveData<PageResponse<ReplyResponse>> replyLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> replyError = new MutableLiveData<>();
    public ReplyViewModel() {
        replyRepository = ReplyRepository.getInstance();
    }


    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }

    public MutableLiveData<PageResponse<ReplyResponse>> getReplyLiveData() {
        return replyLiveData;
    }

    public MutableLiveData<Event<Boolean>> getReplyError() {
        return replyError;
    }

    public void getAllRepliesByTopicId(String topicId, int page) {
        replyRepository.getAllRepliesByTopicId(topicId, page, new Callback<ApiResponse<PageResponse<ReplyResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageResponse<ReplyResponse>>> call, Response<ApiResponse<PageResponse<ReplyResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageResponse<ReplyResponse>> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        replyLiveData.setValue(apiRes.getResult());  // ✅ Không dùng Event
                    } else {
                        replyError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        replyError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageResponse<ReplyResponse>>> call, Throwable throwable) {
                Log.d("Error Topic", throwable.getMessage());
                replyError.setValue(new Event<>(true));
            }
        });
    }
}
