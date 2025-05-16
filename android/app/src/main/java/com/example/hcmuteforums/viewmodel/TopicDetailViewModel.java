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

public class TopicDetailViewModel extends ViewModel {
    private TopicRepository topicRepository;
    private MutableLiveData<Event<Boolean>> likeTopicSuccess = new MutableLiveData<>();
    private MutableLiveData<Event<TopicDetailResponse>> topicDetailLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<String>> deleteLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> deleteError = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> updateError = new MutableLiveData<>();
    private MutableLiveData<Event<TopicDetailResponse>> updateLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> likeTopicError = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> getTopicError = new MutableLiveData<>();
    private MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    public TopicDetailViewModel(){
        topicRepository = TopicRepository.getInstance();
    }
    public void updateTopic(String topicId, String title, String content){
        topicRepository.updateTopic(topicId, title, content, new Callback<ApiResponse<TopicDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<TopicDetailResponse>> call, Response<ApiResponse<TopicDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<TopicDetailResponse> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        updateLiveData.setValue(new Event<>(apiRes.getResult()));  // ✅ Không dùng Event
                    } else {
                        updateError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        updateError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<TopicDetailResponse>> call, Throwable throwable) {
                Log.d("Error Topic", throwable.getMessage());
                updateError.setValue(new Event<>(true));
            }
        });
    }
    public void deleteTopic(String topicId){
        topicRepository.deleteTopic(topicId, new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        deleteLiveData.setValue(new Event<>(apiRes.getResult()));  // ✅ Không dùng Event
                    } else {
                        deleteError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        deleteError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {
                Log.d("Error Topic", throwable.getMessage());
                deleteError.setValue(new Event<>(true));
            }
        });
    }

    public void getTopicDetail(String topicId){
        topicRepository.getTopicDetail(topicId, new Callback<ApiResponse<TopicDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<TopicDetailResponse>> call, Response<ApiResponse<TopicDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<TopicDetailResponse> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        topicDetailLiveData.setValue(new Event<>(apiRes.getResult()));  // ✅ Không dùng Event
                    } else {
                        getTopicError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        getTopicError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<TopicDetailResponse>> call, Throwable throwable) {
                Log.d("Error Topic", throwable.getMessage());
                getTopicError.setValue(new Event<>(true));
            }
        });
    }
    public void likeTopic(String topicId) {
        topicRepository.likeTopic(topicId, new Callback<ApiResponse<Boolean>>() {
            @Override
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
                Log.d("ErrorGetTopic", throwable.getMessage());
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

    public MutableLiveData<Event<TopicDetailResponse>> getTopicDetailLiveData() {
        return topicDetailLiveData;
    }

    public MutableLiveData<Event<Boolean>> getGetTopicError() {
        return getTopicError;
    }

    public MutableLiveData<Event<String>> getDeleteLiveData() {
        return deleteLiveData;
    }

    public MutableLiveData<Event<Boolean>> getDeleteError() {
        return deleteError;
    }

    public MutableLiveData<Event<Boolean>> getUpdateError() {
        return updateError;
    }

    public MutableLiveData<Event<TopicDetailResponse>> getUpdateLiveData() {
        return updateLiveData;
    }

    private MutableLiveData<Event<Boolean>> deleteImagesLiveData = new MutableLiveData<>();

    public MutableLiveData<Event<Boolean>> getDeleteImagesLiveData() {
        return deleteImagesLiveData;
    }

    public void deleteTopicImage(String topicId, List<String> images){
        topicRepository.deleteTopicImages(topicId, images, new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Boolean> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        deleteImagesLiveData.setValue(new Event<>(apiRes.getResult()));  // ✅ Không dùng Event
                    } else {
//                        likeTopicError.setValue(new Event<>(true));       // ✅ Dùng Event
                        Log.d("DeleteImages", "Error");
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        Log.d("DeleteImages", "Error");
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        Log.d("DeleteImages", "Error");

//                        likeTopicError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable throwable) {
                Log.d("DeleteImages", throwable.getMessage());
                likeTopicError.setValue(new Event<>(true));
            }
        });
    }
}
