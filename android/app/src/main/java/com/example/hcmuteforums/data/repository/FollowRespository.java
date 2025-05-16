
package com.example.hcmuteforums.data.repository;
import android.util.Log;

import com.example.hcmuteforums.data.remote.api.FollowApi;
import com.example.hcmuteforums.data.remote.api.SearchApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.request.FollowRequest;
import com.example.hcmuteforums.model.dto.response.FollowResponse;
import com.example.hcmuteforums.model.dto.response.FollowStatusResponse;
import com.example.hcmuteforums.model.dto.response.FollowerResponse;
import com.example.hcmuteforums.model.dto.response.FollowingResponse;

import retrofit2.Call;
import retrofit2.Callback;

public class FollowRespository {
    public static FollowRespository instance;
    FollowApi followApi;
    SearchApi searchApi;
    public FollowRespository(){
        followApi = LocalRetrofit.getRetrofit().create(FollowApi.class);
        searchApi = LocalRetrofit.getRetrofit().create(SearchApi.class);
    }

    public static FollowRespository getInstance(){
        if (instance == null)
            instance = new FollowRespository();
        return instance;
    }
    public void followUser(String username, Callback<ApiResponse<FollowResponse>> callback){
        FollowRequest followRequest = new FollowRequest(username);
        var call = followApi.followUser(followRequest);
        call.enqueue(callback);
    }
    public void unfollowUser(String username , Callback<ApiResponse<FollowResponse>> callback){
        var call = followApi.unfollowUser(username);
        call.enqueue(callback);
    }
    public void getFollower(String username, int page, Callback<ApiResponse<PageResponse<FollowerResponse>>> callback) {
        Call<ApiResponse<PageResponse<FollowerResponse>>> call = followApi.getFollowers(username, page);
        call.enqueue(callback);
    }

    public void getFollowing(String username, int page, Callback<ApiResponse<PageResponse<FollowingResponse>>> callback) {
        Call<ApiResponse<PageResponse<FollowingResponse>>> call = followApi.getFollowings(username, page);
        call.enqueue(callback);
    }
    public void checkFollowStatus(String currentUsername, String targetUsername, Callback<ApiResponse<FollowStatusResponse>> callback){
        Log.d("CurrentUser", currentUsername);
        Log.d("TargetUsername", targetUsername);
        var call = followApi.checkFollowStatus(currentUsername, targetUsername);
        call.enqueue(callback);
    }

    public void getFollowerByUsername(String username, String targetUsername, int page, Callback<ApiResponse<PageResponse<FollowerResponse>>> callback){
        var call = searchApi.getFollowerByUsername(username, targetUsername, page);
        call.enqueue(callback);
    }

    public void getFollowingByUsername(String username, String targetUsername, int page, Callback<ApiResponse<PageResponse<FollowingResponse>>> callback){
        var call = searchApi.getFollowingByUsername(username, targetUsername, page);
        call.enqueue(callback);
    }
}
