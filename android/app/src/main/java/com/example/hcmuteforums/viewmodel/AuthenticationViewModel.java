package com.example.hcmuteforums.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.AuthenticationRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.response.AuthenticationResponse;
import com.example.hcmuteforums.utils.ApiErrorHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationViewModel extends ViewModel {
    private AuthenticationRepository authenticationRepository;
    private MutableLiveData<Event<AuthenticationResponse>> loginResponse = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> loginError = new MutableLiveData<>();
    public AuthenticationViewModel() {
        authenticationRepository = AuthenticationRepository.getInstance();
    }

    // Chúng ta sẽ trả về Event từ Repository để xử lý thông qua LiveData
    public LiveData<Event<AuthenticationResponse>> getLoginResponse() {
        return loginResponse;
    }

    // Tương tự, trả về Event cho lỗi
    public LiveData<Event<Boolean>> getLoginError() {
        return loginError;
    }

    // Hàm login mà ViewModel gọi vào Repository
    public void login(String username, String password) {
        authenticationRepository.login(username, password, new Callback<ApiResponse<AuthenticationResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, Response<ApiResponse<AuthenticationResponse>> response) {
                ApiResponse<?> apiResp;
                if (response.isSuccessful() && response.body() != null){
                    apiResp = response.body();
                }
                else{
                    apiResp = ApiErrorHandler.parseError(response);
                }

                if (apiResp.getCode() == 200){
                    loginResponse.setValue(new Event<>((AuthenticationResponse) apiResp.getResult()));
                }
                else{
                    loginError.setValue(new Event<>(true));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable throwable) {
                Log.d("Loi", throwable.getMessage());
                loginError.setValue(new Event<>(true));
            }
        });
    }

    // Hàm logout (ở đây có thể xử lý logout nếu cần)
    public void logout() {
//        logout();
    }
}

