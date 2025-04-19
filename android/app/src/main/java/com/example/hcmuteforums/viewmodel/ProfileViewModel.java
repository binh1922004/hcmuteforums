package com.example.hcmuteforums.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.ProfileRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.request.ProfileUpdateRequest;
import com.example.hcmuteforums.model.dto.response.ProfileResponse;
import com.example.hcmuteforums.model.dto.response.UserResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileViewModel extends ViewModel {
    private ProfileRepository profileRepository;
    public ProfileViewModel(){
        profileRepository = ProfileRepository.getInstance();
    }
    public LiveData<Event<Boolean>> getProfileInfoError(){
        return profileRepository.getProfileInfoError();
    }
    public LiveData<Event<Boolean>> getProfileUpdateResponse(){
        return profileRepository.getProfileUpdateResponse();
    }
    public LiveData<Event<String>> getMessageError(){
        return profileRepository.getMessageError();
    }
    public LiveData<ProfileResponse> getProfileInfo(){
        return profileRepository.getProfileInfo();
    }
    public LiveData<Event<Boolean>> getProfileUpdateError(){
        return profileRepository.getProfileUpdateError();
    }
    public void getProfile(){
      profileRepository.getProfile();
    }
    public void updateProfile(ProfileUpdateRequest profileUpdateRequest){
        profileRepository.updateProfile(profileUpdateRequest);
    }
    public void uploadCoverImage(File imageFile){
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("avatarImage", imageFile.getName(), requestBody);

        profileRepository.uploadCoverImagae(part);
    }
    public void uploadAvatarImage(File AvatarImageFile)
    {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), AvatarImageFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("avatarImage", AvatarImageFile.getName(), requestBody);

        profileRepository.uploadAvatarImage(part);
    }

}
