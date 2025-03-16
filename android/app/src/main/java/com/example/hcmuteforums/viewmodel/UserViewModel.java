package com.example.hcmuteforums.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.OtpRepository;
import com.example.hcmuteforums.data.repository.UserRepository;
import com.example.hcmuteforums.model.dto.response.UserResponse;

public class UserViewModel extends ViewModel {
    private UserRepository userRepository;
    public UserViewModel(){
        userRepository = UserRepository.getInstance();
    }

    public LiveData<Boolean> getUserInfoError(){
        return userRepository.getUserInfoError();
    }
    public LiveData<String> getMessageError(){
        return userRepository.getMessageError();
    }
    public LiveData<UserResponse> getUserInfo(){
        return userRepository.getUserInfo();
    }
    public void getInfo() {
        userRepository.getInfo();
    }





}
