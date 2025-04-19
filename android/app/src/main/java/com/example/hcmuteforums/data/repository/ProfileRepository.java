package com.example.hcmuteforums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.ProfileApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.ProfileUpdateRequest;
import com.example.hcmuteforums.model.dto.response.ProfileResponse;
import com.example.hcmuteforums.model.dto.response.UserResponse;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    public static ProfileRepository instance;
    private final MutableLiveData<ProfileResponse> profileInfo = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> messageError = new MutableLiveData<>();

    private final MutableLiveData<Event<Boolean>> profileInfoError = new MutableLiveData<>();

    private final MutableLiveData<Event<Boolean>> profileUpdateResponse = new MutableLiveData<>();

    private final MutableLiveData<Event<Boolean>> profileUpdateError = new MutableLiveData<>();

    private final ProfileApi profileApi;

    public static ProfileRepository getInstance() {
        if (instance == null)
            instance = new ProfileRepository();
        return instance;
    }

    public ProfileRepository() {
        profileApi = LocalRetrofit.getRetrofit().create(ProfileApi.class);
    }

    public void getProfile() {
        profileApi.myProfile().enqueue(new Callback<ApiResponse<ProfileResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProfileResponse>> call, Response<ApiResponse<ProfileResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ProfileResponse> apiResponse = response.body();
                    if (apiResponse.getResult() != null) {
                        profileInfo.setValue(apiResponse.getResult());
                    } else {
                        profileInfoError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProfileResponse>> call, Throwable throwable) {
                Log.d("Error UserInfo", throwable.getMessage());
                profileInfoError.setValue(new Event<>(true));
            }
        });
    }

    public void updateProfile(ProfileUpdateRequest profileUpdateRequest) {
        profileApi.updateProfile(profileUpdateRequest).enqueue(new Callback<ApiResponse<ProfileResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProfileResponse>> call, Response<ApiResponse<ProfileResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ProfileResponse> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        profileUpdateResponse.setValue(new Event<>(true));
                    } else {
                        profileUpdateError.setValue(new Event<>(true));
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        if (apiError.getMessage() != null && !apiError.getMessage().trim().isEmpty()) {
                            messageError.setValue(new Event<>(apiError.getMessage()));
                        } else {
                            messageError.setValue(new Event<>("Đã xảy ra lỗi không xác định."));
                        }
                    } else {
                        profileUpdateResponse.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProfileResponse>> call, Throwable throwable) {
                Log.d("Error Profile User", throwable.getMessage());
                profileUpdateError.setValue(new Event<>(true));
            }
        });
    }

    public void uploadAvatarImage(MultipartBody.Part avatarPart)
    {
        Call<ApiResponse<Boolean>> call = profileApi.uploadAvatarImage(avatarPart);
        call.enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    profileUpdateResponse.postValue(new Event<>(response.body().getResult()));
                }else {
                    profileUpdateError.postValue(new Event<>(true));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable throwable) {
                messageError.postValue(new Event<>("Upload ảnh bìa thất bại: " + throwable.getMessage()));
            }
        });
    }
    public void uploadCoverImagae(MultipartBody.Part coverPart){
        Call<ApiResponse<Boolean>> call = profileApi.uploadCoverImage(coverPart);
        call.enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    profileUpdateResponse.postValue(new Event<>(response.body().getResult()));
                }else {
                    profileUpdateError.postValue(new Event<>(true));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable throwable) {
                messageError.postValue(new Event<>("Upload ảnh bìa thất bại: " + throwable.getMessage()));
            }
        });
    }

    public MutableLiveData<ProfileResponse> getProfileInfo() {
        return profileInfo;
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Event<Boolean>> getProfileInfoError() {
        return profileInfoError;
    }

    public MutableLiveData<Event<Boolean>> getProfileUpdateResponse() {
        return profileUpdateResponse;
    }

    public MutableLiveData<Event<Boolean>> getProfileUpdateError() {
        return profileUpdateError;
    }
}
