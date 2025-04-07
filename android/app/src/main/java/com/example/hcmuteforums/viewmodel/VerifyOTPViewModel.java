package com.example.hcmuteforums.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.UserRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.request.UserCreationRequest;

public class VerifyOTPViewModel extends ViewModel {
    private UserRepository userRepository;
    public VerifyOTPViewModel(){
        userRepository = UserRepository.getInstance();
    }

    public void register(UserCreationRequest userCreationRequest){
        userRepository.register(userCreationRequest);
    }

    public LiveData<Event<Boolean>> getRegisterResponse() {
        return userRepository.getRegisterResponse();
    }

    public LiveData<Event<String>> getMessageError() {
        return userRepository.getMessageError();
    }

    public LiveData<Event<Boolean>> getRegisterError() {
        return userRepository.getRegisterError();
    }
}
