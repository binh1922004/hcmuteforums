    package com.example.hcmuteforums.viewmodel;

    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.ViewModel;

    import com.example.hcmuteforums.data.repository.OtpRepository;
    import com.example.hcmuteforums.data.repository.UserRepository;
    import com.example.hcmuteforums.event.Event;
    import com.example.hcmuteforums.model.dto.request.UserUpdateRequest;
    import com.example.hcmuteforums.model.dto.response.UserResponse;

    public class UserViewModel extends ViewModel {
        private UserRepository userRepository;
        public UserViewModel(){
            userRepository = UserRepository.getInstance();
        }

        public LiveData<Event<Boolean>> getUserInfoError(){
            return userRepository.getUserInfoError();
        }
        public LiveData<Event<String>> getMessageError(){
            return userRepository.getMessageError();
        }
        public LiveData<UserResponse> getUserInfo(){
            return userRepository.getUserInfo();
        }
        public void getInfo() {
            userRepository.getInfo();
        }
        public void updateUser(UserUpdateRequest userUpdateRequest){
            userRepository.updateUser(userUpdateRequest);
        }
        public LiveData<Event<Boolean>> getUserUpdateError(){
            return userRepository.getUserUpdateError();
        }
        public LiveData<Event<Boolean>> getUserUpdate(){
            return userRepository.getUpdateResponse();
        }






    }
