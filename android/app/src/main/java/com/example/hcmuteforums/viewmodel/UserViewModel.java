    package com.example.hcmuteforums.viewmodel;

    import android.util.Log;

    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.MutableLiveData;
    import androidx.lifecycle.ViewModel;

    import com.example.hcmuteforums.data.repository.OtpRepository;
    import com.example.hcmuteforums.data.repository.UserRepository;
    import com.example.hcmuteforums.event.Event;
    import com.example.hcmuteforums.model.dto.ApiErrorResponse;
    import com.example.hcmuteforums.model.dto.ApiResponse;
    import com.example.hcmuteforums.model.dto.request.UserUpdateRequest;
    import com.example.hcmuteforums.model.dto.response.UserResponse;
    import com.google.gson.Gson;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class UserViewModel extends ViewModel {
        private UserRepository userRepository;

        private final MutableLiveData<Event<Boolean>> registerResponse = new MutableLiveData<>();
        private final MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
        private final MutableLiveData<Event<Boolean>> registerError = new MutableLiveData<>();

        private final MutableLiveData<UserResponse> userInfo = new MutableLiveData<>();
        private final MutableLiveData<Event<Boolean>> userInfoError = new MutableLiveData<>();

        private final MutableLiveData<Event<Boolean>> userUpdateError = new MutableLiveData<>();
        private final MutableLiveData<Event<Boolean>> updateResponse = new MutableLiveData<>();
        public UserViewModel(){
            userRepository = UserRepository.getInstance();
        }

        public void getInfo() {
            userRepository.getInfo(new Callback<ApiResponse<UserResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<UserResponse> apiRes = response.body();
                        if (apiRes.getResult() != null) {
                            userInfo.setValue(apiRes.getResult());
                        } else {
                            userInfoError.setValue(new Event<>(true));
                        }
                    } else {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                            messageError.setValue(new Event<>(apiError.getMessage()));
                        } else {
                            userInfoError.setValue(new Event<>(true));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable throwable) {
                    Log.d("Error UserInfo", throwable.getMessage());
                    userInfoError.setValue(new Event<>(true));
                }
            });
        }
        public void updateUser(UserUpdateRequest userUpdateRequest){
            userRepository.updateUser(userUpdateRequest, new Callback<ApiResponse<UserResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<UserResponse> apiRes = response.body();
                        if (apiRes.getResult() != null) {
                            updateResponse.setValue(new Event<>(true));
                        } else {
                            userUpdateError.setValue(new Event<>(true));
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
                            updateResponse.setValue(new Event<>(true));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable throwable) {
                    Log.d("Error Update User", throwable.getMessage());
                    userUpdateError.setValue(new Event<>(true));
                }
            });
        }

        public MutableLiveData<Event<Boolean>> getRegisterResponse() {
            return registerResponse;
        }

        public MutableLiveData<Event<String>> getMessageError() {
            return messageError;
        }

        public MutableLiveData<Event<Boolean>> getRegisterError() {
            return registerError;
        }

        public MutableLiveData<UserResponse> getUserInfo() {
            return userInfo;
        }

        public MutableLiveData<Event<Boolean>> getUserInfoError() {
            return userInfoError;
        }

        public MutableLiveData<Event<Boolean>> getUserUpdateError() {
            return userUpdateError;
        }

        public MutableLiveData<Event<Boolean>> getUserUpdate() {
            return updateResponse;
        }
    }
