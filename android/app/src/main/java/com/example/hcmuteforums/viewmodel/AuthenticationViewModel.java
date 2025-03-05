package com.example.hcmuteforums.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.AuthenticationRepository;
import com.example.hcmuteforums.model.ApiResponse;
import com.example.hcmuteforums.model.dto.response.AuthenticationResponse;

public class AuthenticationViewModel extends ViewModel {
    private AuthenticationRepository authenticationRepository;
    public AuthenticationViewModel(){
        authenticationRepository = AuthenticationRepository.getInstance();
    }
    public LiveData<ApiResponse<AuthenticationResponse>> getLoginResponse(){
        return authenticationRepository.getLoginResponse();
    }

    public LiveData<Boolean> getLoginError(){
        return authenticationRepository.getLoginError();
    }

    public void login(String username, String password){
        authenticationRepository.login(username, password);
    }
}
