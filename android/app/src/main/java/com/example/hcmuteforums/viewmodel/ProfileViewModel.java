package com.example.hcmuteforums.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.ProfileRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.request.ProfileUpdateRequest;
import com.example.hcmuteforums.model.dto.response.ProfileResponse;
import com.example.hcmuteforums.model.dto.response.UserResponse;
import com.google.gson.Gson;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    private ProfileRepository profileRepository;
    public ProfileViewModel(){
        profileRepository = ProfileRepository.getInstance();
    }
    private final MutableLiveData<ProfileResponse> profileInfo = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> messageSuccess = new MutableLiveData<>();

    private final MutableLiveData<Event<Boolean>> profileInfoError = new MutableLiveData<>();

    private final MutableLiveData<Event<Boolean>> profileUpdateResponse = new MutableLiveData<>();

    private final MutableLiveData<Event<Boolean>> profileUpdateError = new MutableLiveData<>();

    public LiveData<Event<Boolean>> getProfileInfoError(){
        return profileInfoError;
    }
    public LiveData<Event<Boolean>> getProfileUpdateResponse(){
        return profileUpdateResponse;
    }
    public LiveData<Event<String>> getMessageError(){
        return messageError;
    }
    public LiveData<ProfileResponse> getProfileInfo(){
        return profileInfo;
    }
    public LiveData<Event<Boolean>> getProfileUpdateError(){
        return profileUpdateError;
    }
    public void getProfile(){
      profileRepository.getProfile(new Callback<ApiResponse<ProfileResponse>>() {
          @Override
          public void onResponse(Call<ApiResponse<ProfileResponse>> call, Response<ApiResponse<ProfileResponse>> response) {
              ApiResponse<ProfileResponse> apiResponse = response.body();
              if (apiResponse.getResult() != null) {
                  profileInfo.setValue(apiResponse.getResult());
              } else {
                  profileInfoError.setValue(new Event<>(true));
              }
          }

          @Override
          public void onFailure(Call<ApiResponse<ProfileResponse>> call, Throwable throwable) {
              Log.d("Error UserInfo", throwable.getMessage());
              profileInfoError.setValue(new Event<>(true));
          }
      });
    }
    public void updateProfile(String bio){
        profileRepository.updateProfile(bio, new Callback<ApiResponse<ProfileResponse>>() {
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
    public void uploadCoverImage(File imageFile){
      profileRepository.uploadCoverImagae(imageFile, new Callback<ApiResponse<Boolean>>() {
          @Override
          public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
              if(response.isSuccessful() && response.body()!=null){
                  profileUpdateResponse.postValue(new Event<>(response.body().getResult()));
                  messageSuccess.postValue(new Event<>("Cập nhật ảnh đại diện thành công"));
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
    public void uploadAvatarImage(File AvatarImageFile)
    {
        profileRepository.uploadAvatarImage(AvatarImageFile, new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    profileUpdateResponse.postValue(new Event<>(response.body().getResult()));
                    messageSuccess.postValue(new Event<>("Cập nhật ảnh bìa thành công"));
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

}
