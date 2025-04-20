package com.example.hcmuteforums.data.repository;

import android.content.Context;
import android.net.Uri;

import com.example.hcmuteforums.data.remote.api.LikeApi;
import com.example.hcmuteforums.data.remote.api.ReplyApi;
import com.example.hcmuteforums.data.remote.api.TopicApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.TopicPostRequest;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;
import com.example.hcmuteforums.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;

public class ReplyRepository {
    private static ReplyRepository instance;
    private ReplyApi replyApi;

    public static ReplyRepository getInstance() {
        if (instance == null)
            instance = new ReplyRepository();
        return instance;
    }

    public ReplyRepository() {
        replyApi = LocalRetrofit.getRetrofit().create(ReplyApi.class);
    }

    public void getAllRepliesByTopicId(String topicId, Callback<ApiResponse<List<ReplyResponse>>> callback) {
        var call = replyApi.getAllRepliesByTopicId(topicId);
        call.enqueue(callback);
    }

}
