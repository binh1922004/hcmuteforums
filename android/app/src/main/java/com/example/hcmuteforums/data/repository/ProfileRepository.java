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
    private final ProfileApi profileApi;

    public static ProfileRepository getInstance() {
        if (instance == null)
            instance = new ProfileRepository();
        return instance;
    }

    public ProfileRepository() {
        profileApi = LocalRetrofit.getRetrofit().create(ProfileApi.class);
    }

    public void getProfile(Callback<ApiResponse<ProfileResponse>> callback) {
        var call = profileApi.myProfile();
        call.enqueue(callback);
    }

    public void updateProfile(String bio,Callback<ApiResponse<ProfileResponse>> callback) {
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(bio);
        var call = profileApi.updateProfile(profileUpdateRequest);
        call.enqueue(callback);
    }

    public void uploadAvatarImage(File AvatarImageFile, Callback<ApiResponse<Boolean>> callback)
    {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), AvatarImageFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", AvatarImageFile.getName(), requestBody);
        var call = profileApi.uploadAvatarImage(part);
        call.enqueue(callback);
    }
    public void uploadCoverImagae(File CoverimageFile, Callback<ApiResponse<Boolean>> callback){

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), CoverimageFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", CoverimageFile.getName(), requestBody);
        var call = profileApi.uploadCoverImage(part);
        call.enqueue(callback);
    }

    }
