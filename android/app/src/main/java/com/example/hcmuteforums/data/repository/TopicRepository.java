package com.example.hcmuteforums.data.repository;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.LikeApi;
import com.example.hcmuteforums.data.remote.api.TopicApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.request.TopicPostRequest;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;
import com.example.hcmuteforums.utils.FileUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicRepository {
    private static TopicRepository instance;

    private TopicApi topicApi;
    private LikeApi likeApi;

    public static TopicRepository getInstance() {
        if (instance == null)
            instance = new TopicRepository();
        return instance;
    }

    public TopicRepository() {
        topicApi = LocalRetrofit.getRetrofit().create(TopicApi.class);
        likeApi = LocalRetrofit.getRetrofit().create(LikeApi.class);
    }

    public void getAllTopics(int page, Callback<ApiResponse<PageResponse<TopicDetailResponse>>> callback) {
        var call = topicApi.getAllTopic(page);
        call.enqueue(callback);
    }

    public void postTopic(String title, String content, Callback<ApiResponse<TopicDetailResponse>> callback){
        TopicPostRequest topicPostRequest = new TopicPostRequest(title, content);
        var call = topicApi.postTopic(topicPostRequest);
        call.enqueue(callback);
    }

    public void uploadImage(String topicId, List<Uri> imageUris, Context context, Callback<ApiResponse<Boolean>> callback){
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (Uri uri : imageUris) {
            File file = FileUtils.getFileFromUri(context, uri);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
            parts.add(part);
        }
        var call = topicApi.uploadImages(topicId, parts);
        call.enqueue(callback);
    }

    public void likeTopic(String topicId, Callback<ApiResponse<Boolean>> callback){
        var call = likeApi.likeTopic(topicId);
        call.enqueue(callback);
    }

}
