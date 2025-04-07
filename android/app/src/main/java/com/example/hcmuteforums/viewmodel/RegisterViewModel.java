package com.example.hcmuteforums.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.OtpRepository;
import com.example.hcmuteforums.event.Event;

public class RegisterViewModel extends ViewModel {
    private OtpRepository otpRepository;
    public RegisterViewModel(){
        otpRepository = OtpRepository.getInstance();
    }

    public void sendOtp(String email, String username){
        otpRepository.sendOtp(email, username);
    }
    public LiveData<Event<Boolean>> getSendOtpResponse(){
        return otpRepository.getSendOtpResponse();
    }
    public LiveData<Event<Boolean>> getSendOtpError(){
        return otpRepository.getSendOtpError();
    }

    public LiveData<Event<String>> getMessageError(){
        return otpRepository.getMessageError();
    }

    public LiveData<Boolean> getIsLoading(){
        return otpRepository.getIsLoading();
    }

}
