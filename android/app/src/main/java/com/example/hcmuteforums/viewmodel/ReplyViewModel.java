package com.example.hcmuteforums.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.ReplyRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReplyViewModel extends ViewModel {
    private ReplyRepository replyRepository;

    private MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    private MutableLiveData<Event<String>> messageChildError = new MutableLiveData<>();
    private MutableLiveData<PageResponse<ReplyResponse>> replyLiveData = new MutableLiveData<>();
    private MutableLiveData<PageResponse<ReplyResponse>> replyChildLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> replyError = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> replyChildError = new MutableLiveData<>();
    private MutableLiveData<Event<ReplyResponse>> replyPostLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<ReplyResponse>> replyUpdateLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> replyUpdateError = new MutableLiveData<>();
    private MutableLiveData<Event<String>> replyDeleteLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> replyDeleteError = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Event<ReplyResponse>> detailReplyLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> detailReplyError= new MutableLiveData<>();

    public ReplyViewModel() {
        replyRepository = ReplyRepository.getInstance();
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }

    public MutableLiveData<PageResponse<ReplyResponse>> getReplyLiveData() {
        return replyLiveData;
    }

    public MutableLiveData<Event<ReplyResponse>> getReplyPostLiveData() {
        return replyPostLiveData;
    }

    public MutableLiveData<Event<Boolean>> getReplyError() {
        return replyError;
    }

    public MutableLiveData<Event<String>> getMessageChildError() {
        return messageChildError;
    }

    public MutableLiveData<PageResponse<ReplyResponse>> getReplyChildLiveData() {
        return replyChildLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Event<ReplyResponse>> getDetailReplyLiveData() {
        return detailReplyLiveData;
    }

    public MutableLiveData<Event<Boolean>> getDetailReplyError() {
        return detailReplyError;
    }

    public MutableLiveData<Event<Boolean>> getReplyChildError() {
        return replyChildError;
    }

    public MutableLiveData<Event<ReplyResponse>> getReplyUpdateLiveData() {
        return replyUpdateLiveData;
    }

    public MutableLiveData<Event<String>> getReplyDeleteLiveData() {
        return replyDeleteLiveData;
    }

    public MutableLiveData<Event<Boolean>> getReplyUpdateError() {
        return replyUpdateError;
    }

    public MutableLiveData<Event<Boolean>> getReplyDeleteError() {
        return replyDeleteError;
    }
    public void deleteReply(String replyId){
        replyRepository.deleteReply(replyId, new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        replyDeleteLiveData.setValue(new Event<>(apiRes.getResult()));  // ✅ Không dùng Event
                    } else {
                        replyDeleteError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        replyDeleteError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {
                Log.d("Error Topic", throwable.getMessage());
                replyDeleteError.setValue(new Event<>(true));
            }
        });
    }
    public void updateReply(String replyId, String content){
        replyRepository.updateReply(replyId, content, new Callback<ApiResponse<ReplyResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ReplyResponse>> call, Response<ApiResponse<ReplyResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ReplyResponse> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        replyUpdateLiveData.setValue(new Event<>(apiRes.getResult()));  // ✅ Không dùng Event
                    } else {
                        replyUpdateError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        replyUpdateError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ReplyResponse>> call, Throwable throwable) {
                Log.d("Error Topic", throwable.getMessage());
                replyUpdateError.setValue(new Event<>(true));
            }
        });
    }

    public void getAllRepliesByTopicId(String topicId, int page) {
        isLoading.setValue(true);
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
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<ApiResponse<PageResponse<ReplyResponse>>> call, Throwable throwable) {
                Log.d("Error Topic", throwable.getMessage());
                replyError.setValue(new Event<>(true));
                isLoading.setValue(false);
            }
        });
    }
    public void getDetailReply(String replyId) {
        replyRepository.getDetailReply(replyId, new Callback<ApiResponse<ReplyResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ReplyResponse>> call, Response<ApiResponse<ReplyResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ReplyResponse> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        detailReplyLiveData.setValue(new Event<>(apiRes.getResult()));
                    } else {
                        detailReplyError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        detailReplyError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ReplyResponse>> call, Throwable throwable) {
                Log.d("Error Reply", throwable.getMessage());
                detailReplyError.setValue(new Event<>(true));
            }
        });
    }
    public void postReply(String content, String parentId, String targetUserName, String topicId){
        replyRepository.postReply(content, parentId, targetUserName, topicId, new Callback<ApiResponse<ReplyResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ReplyResponse>> call, Response<ApiResponse<ReplyResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ReplyResponse> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        replyPostLiveData.setValue(new Event<>(apiRes.getResult()));  // ✅ Không dùng Event
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
            public void onFailure(Call<ApiResponse<ReplyResponse>> call, Throwable throwable) {
                Log.d("Error Topic", throwable.getMessage());
                replyError.setValue(new Event<>(true));
            }
        });
    }
    public void getAllRepliesByParentReplyId(String parentId, int page){
        replyRepository.getAllRepliesByParentReplyId(parentId, page, new Callback<ApiResponse<PageResponse<ReplyResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageResponse<ReplyResponse>>> call, Response<ApiResponse<PageResponse<ReplyResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageResponse<ReplyResponse>> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        replyChildLiveData.setValue(apiRes.getResult());  // ✅ Không dùng Event
                    } else {
                        replyChildError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageChildError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        replyChildError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageResponse<ReplyResponse>>> call, Throwable throwable) {
                Log.d("ErrorChildReply", throwable.getMessage());
                replyChildError.setValue(new Event<>(true));
            }
        });
    }
}
