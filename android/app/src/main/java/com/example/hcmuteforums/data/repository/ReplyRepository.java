package com.example.hcmuteforums.data.repository;



import com.example.hcmuteforums.data.remote.api.ReplyApi;

import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.request.ReplyPostRequest;

import com.example.hcmuteforums.model.dto.response.ReplyResponse;

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

    public void getAllRepliesByTopicId(String topicId, int page, Callback<ApiResponse<PageResponse<ReplyResponse>>> callback) {
        var call = replyApi.getAllRepliesByTopicId(topicId, page);
        call.enqueue(callback);
    }

    public void getDetailReply(String replyId, Callback<ApiResponse<ReplyResponse>> callback){
        var call = replyApi.getDetailReply(replyId);
        call.enqueue(callback);
    }
    public void postReply(String content, String parentId, String targetUserName, String topicId, Callback<ApiResponse<ReplyResponse>> callback){
        ReplyPostRequest request = new ReplyPostRequest(content, parentId, targetUserName, topicId);
        var call = replyApi.postReplyTopic(request);
        call.enqueue(callback);
    }

    public void getAllRepliesByParentReplyId(String parentReplyId, int page, Callback<ApiResponse<PageResponse<ReplyResponse>>> callback){
        var call = replyApi.getAllRepliesByParentReplyId(parentReplyId, page);
        call.enqueue(callback);
    }

    public void updateReply(String replyId, String content, Callback<ApiResponse<ReplyResponse>> callback){
        var call = replyApi.updateReply(replyId, content);
        call.enqueue(callback);
    }

    public void deleteReply(String replyId, Callback<ApiResponse<String>> callback){
        var call = replyApi.deleteReply(replyId);
        call.enqueue(callback);
    }
}
