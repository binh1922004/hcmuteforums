package com.example.hcmuteforums.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.OtpRepository;

public class RegisterViewModel extends ViewModel {
    private OtpRepository otpRepository;
    public RegisterViewModel(){
        otpRepository = OtpRepository.getInstance();
    }

    public void sendOtp(String email, String username){
        otpRepository.sendOtp(email, username);
    }
    public LiveData<Boolean> getSendOtpResponse(){
        return otpRepository.getSendOtpResponse();
    }
    public LiveData<Boolean> getSendOtpError(){
        return otpRepository.getSendOtpError();
    }

    public LiveData<String> getMessageError(){
        return otpRepository.getMessageError();
    }

    public LiveData<Boolean> getIsLoading(){
        return otpRepository.getIsLoading();
    }

}
