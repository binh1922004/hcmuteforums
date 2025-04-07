package com.example.hcmuteforums.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.AuthenticationRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.response.AuthenticationResponse;

public class AuthenticationViewModel extends ViewModel {
    private AuthenticationRepository authenticationRepository;

    public AuthenticationViewModel() {
        authenticationRepository = AuthenticationRepository.getInstance();
    }

    // Chúng ta sẽ trả về Event từ Repository để xử lý thông qua LiveData
    public LiveData<Event<AuthenticationResponse>> getLoginResponse() {
        return authenticationRepository.getLoginResponse();
    }

    // Tương tự, trả về Event cho lỗi
    public LiveData<Event<Boolean>> getLoginError() {
        return authenticationRepository.getLoginError();
    }

    // Hàm login mà ViewModel gọi vào Repository
    public void login(String username, String password) {
        authenticationRepository.login(username, password);
    }

    // Hàm logout (ở đây có thể xử lý logout nếu cần)
    public void logout() {
        authenticationRepository.logout();
    }
}

