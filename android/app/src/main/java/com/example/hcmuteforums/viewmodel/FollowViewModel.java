package com.example.hcmuteforums.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.FollowRespository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.request.FollowRequest;
import com.example.hcmuteforums.model.dto.response.FollowResponse;
import com.example.hcmuteforums.model.dto.response.FollowStatusResponse;
import com.example.hcmuteforums.model.dto.response.FollowerResponse;
import com.example.hcmuteforums.model.dto.response.FollowingResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowViewModel extends ViewModel {
    private FollowRespository followRespository;
    public FollowViewModel(){followRespository=FollowRespository.getInstance();}
    private MutableLiveData<Event<Boolean>> toFollowSuccess = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> toFollowError = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> unFollowSuccess = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> unFollowError = new MutableLiveData<>();
    private MutableLiveData<PageResponse<FollowerResponse>> getListFollower = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> getFollowerError = new MutableLiveData<>();
    private MutableLiveData<PageResponse<FollowingResponse>> getListFollowing = new MutableLiveData<>();
    private MutableLiveData<PageResponse<FollowingResponse>> getListFollowingCurrentUser = new MutableLiveData<>(); // Dành cho currentUser
    private  MutableLiveData<Boolean> followStatus = new MutableLiveData<>();
    private  MutableLiveData<Event<Boolean>> errorFollowStatus = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> getFollowingError = new MutableLiveData<>();
    private MutableLiveData<Event<String>> messageError = new MutableLiveData<>();

    public MutableLiveData<Event<Boolean>> getToFollowSuccess() {
        return toFollowSuccess;
    }

    public MutableLiveData<Event<Boolean>> getToFollowError() {
        return toFollowError;
    }

    public MutableLiveData<Event<Boolean>> getUnFollowSuccess() {
        return unFollowSuccess;
    }

    public MutableLiveData<Event<Boolean>> getUnFollowError() {
        return unFollowError;
    }

    public MutableLiveData<Event<Boolean>> getErrorFollowStatus() {
        return errorFollowStatus;
    }
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getFollowStatus() {
        return followStatus;
    }
    public MutableLiveData<PageResponse<FollowerResponse>> getGetListFollower() {
        return getListFollower;
    }

    public MutableLiveData<PageResponse<FollowingResponse>> getGetListFollowingCurrentUser() {
        return getListFollowingCurrentUser;
    }

    public MutableLiveData<Event<Boolean>> getGetFollowerError() {
        return getFollowerError;
    }

    public MutableLiveData<PageResponse<FollowingResponse>> getGetListFollowing() {
        return getListFollowing;
    }

    public MutableLiveData<Event<Boolean>> getGetFollowingError() {
        return getFollowingError;
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }
    public void toFollow(String username){
        followRespository.followUser(username, new Callback<ApiResponse<FollowResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<FollowResponse>> call, Response<ApiResponse<FollowResponse>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    ApiResponse<FollowResponse> apiRes = response.body();
                    if(apiRes.getResult()!=null){
                        toFollowSuccess.setValue(new Event<>(true));
                    }else{
                        toFollowError.setValue(new Event<>(true));
                    }
                }else{
                    if(response.errorBody()!=null){
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));
                    }else{
                        toFollowError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<FollowResponse>> call, Throwable throwable) {
                Log.d("Error ToFollow", throwable.getMessage());
                toFollowError.setValue(new Event<>(true));
            }
        });
    }
    public void unFollow(String username){
        followRespository.unfollowUser(username, new Callback<ApiResponse<FollowResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<FollowResponse>> call, Response<ApiResponse<FollowResponse>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    ApiResponse<FollowResponse> apiResponse = response.body();
                    if(apiResponse.getResult()!=null){
                        unFollowSuccess.setValue(new Event<>(true));
                    }else{
                        unFollowError.setValue(new Event<>(true));
                    }
                }else{
                    if(response.errorBody()!=null){
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));
                    }else {
                        unFollowError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<FollowResponse>> call, Throwable throwable) {
                Log.d("Error unFollow", throwable.getMessage());
                unFollowError.setValue(new Event<>(true));
            }
        });
    }
    public void getFollower(String username, int page){
        isLoading.setValue(true);
        followRespository.getFollower(username, page, new Callback<ApiResponse<PageResponse<FollowerResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageResponse<FollowerResponse>>> call, Response<ApiResponse<PageResponse<FollowerResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageResponse<FollowerResponse>> apiResponse = response.body();
                    if(apiResponse.getResult()!=null){
                        getListFollower.setValue(apiResponse.getResult());
                    }else{
                        getFollowerError.setValue(new Event<>(true));
                    }

                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        Log.d("Message", apiError.getMessage());
                        messageError.setValue(new Event<>(apiError.getMessage()));
                    }
                    getFollowerError.setValue(new Event<>(true));
                }
                isLoading.setValue(false);

            }

            @Override
            public void onFailure(Call<ApiResponse<PageResponse<FollowerResponse>>> call, Throwable throwable) {
                Log.e("Get Followers", throwable.getMessage());
                getFollowerError.setValue(new Event<>(true));
                isLoading.setValue(false);
            }
        });
    }
    public void getFollowing(String username, int page){
        isLoading.setValue(true);
        followRespository.getFollowing(username, page, new Callback<ApiResponse<PageResponse<FollowingResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageResponse<FollowingResponse>>> call, Response<ApiResponse<PageResponse<FollowingResponse>>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    ApiResponse<PageResponse<FollowingResponse>> apiResponse = response.body();
                    if(apiResponse.getResult()!=null){
                        if (username.equals("default_user")) { // Giả định "default_user" là currentUserUsername
                            getListFollowingCurrentUser.setValue(apiResponse.getResult());
                        } else {
                            getListFollowing.setValue(apiResponse.getResult());
                        }

                    }else{
                        getFollowingError.setValue(new Event<>(true));
                    }
                }else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));
                    }
                    getFollowingError.setValue(new Event<>(true));
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<ApiResponse<PageResponse<FollowingResponse>>> call, Throwable throwable) {
                Log.e("Get Followings", throwable.getMessage());
                getFollowingError.setValue(new Event<>(true));
                isLoading.setValue(false);
            }
        });
    }
    public void checkFollowStatus(String currentUsername, String targetUsername, boolean isLoggedIn) {
        if (!isLoggedIn || currentUsername == null || targetUsername == null || currentUsername.equals(targetUsername)) {
            followStatus.setValue(false);
            Log.d("FollowViewModel", "Invalid input or guest detected, skipping checkFollowStatus for " + currentUsername + " and " + targetUsername);
            return;
        }

        // Đặt trạng thái ban đầu để báo hiệu đang kiểm tra
        followStatus.setValue(null);
        Log.d("FollowViewModel", "Checking follow status for " + currentUsername + " and " + targetUsername);

        followRespository.checkFollowStatus(currentUsername, targetUsername, new Callback<ApiResponse<FollowStatusResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<FollowStatusResponse>> call, Response<ApiResponse<FollowStatusResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<FollowStatusResponse> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        boolean isFollowing = apiRes.getResult().isFollowing();
                        followStatus.setValue(isFollowing);
                        Log.d("FollowViewModel", "Check follow status success: " + currentUsername + " isFollowing: " + isFollowing + " for " + targetUsername);
                    } else {
                        followStatus.setValue(false); // Trạng thái mặc định nếu không có dữ liệu
                        errorFollowStatus.setValue(new Event<>(true));
                        Log.e("FollowViewModel", "No follow status data for " + targetUsername);
                    }
                } else {
                    followStatus.setValue(false); // Trạng thái mặc định khi lỗi
                    if (response.errorBody() != null) {
                        try {
                            Gson gson = new Gson();
                            ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                            messageError.setValue(new Event<>(apiError.getMessage()));
                        } catch (Exception e) {
                            messageError.setValue(new Event<>("Unknown error parsing error response"));
                        }
                    }
                    errorFollowStatus.setValue(new Event<>(true));
                    Log.e("FollowViewModel", "Failed to check follow status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<FollowStatusResponse>> call, Throwable throwable) {
                followStatus.setValue(false); // Trạng thái mặc định khi thất bại
                messageError.setValue(new Event<>("Network error: " + throwable.getMessage()));
                errorFollowStatus.setValue(new Event<>(true));
                Log.e("FollowViewModel", "Network error checking follow status: " + throwable.getMessage());
            }
        });
    }


}
