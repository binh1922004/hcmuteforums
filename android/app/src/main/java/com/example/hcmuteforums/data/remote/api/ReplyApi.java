package com.example.hcmuteforums.data.remote.api;

import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.request.ReplyPostRequest;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReplyApi {
    @GET("api/reply/{topicId}")
    Call<ApiResponse<PageResponse<ReplyResponse>>> getAllRepliesByTopicId(@Path("topicId") String topicId, @Query("page") int page);

    @POST("api/reply")
    Call<ApiResponse<ReplyResponse>> postReplyTopic(@Body ReplyPostRequest replyPostRequest);

    @GET("api/reply/parent/{parentReplyId}")
    Call<ApiResponse<PageResponse<ReplyResponse>>> getAllRepliesByParentReplyId(@Path("parentReplyId") String parentReplyId, @Query("page") int page);
    @GET("api/reply/detail/{replyId}")
    Call<ApiResponse<ReplyResponse>> getDetailReply(@Path("replyId") String replyId);


    @PUT("api/reply/update")
    Call<ApiResponse<ReplyResponse>> updateReply(@Query("replyId") String replyId, @Query("content") String content);
    @DELETE("api/reply/delete")
    Call<ApiResponse<String>> deleteReply(@Query("replyId") String replyId);

}
