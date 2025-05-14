package com.backend.backend.service;

import com.backend.backend.dto.request.PasswordUpdateRequest;
import com.backend.backend.dto.request.UserCreationRequest;
import com.backend.backend.dto.request.UserUpdateRequest;
import com.backend.backend.dto.response.UserResponse;
import com.backend.backend.entity.User;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.mapper.UserMapper;
import com.backend.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    //repository
    UserRepository userRepository;
    //pass encode
    PasswordEncoder passwordEncoder;
    //mapper
    UserMapper userMapper;
    //another service
    ProfileService profileService;
    OtpService otpService;

    public boolean createUser(UserCreationRequest userCreationRequest) {
        if (!otpService.validateOtp(userCreationRequest.getEmail(), userCreationRequest.getOtp())){
            return false;
        }

        userCreationRequest.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        User user = userMapper.toUser(userCreationRequest);

        return profileService.createProfile(userRepository.save(user)) != null;
    }
    public boolean updatePassword(PasswordUpdateRequest passwordUpdateRequest) {
        String username = passwordUpdateRequest.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()->
                new AppException(ErrorCode.USER_NOTEXISTED));
        passwordUpdateRequest.setPassword(passwordEncoder.encode(passwordUpdateRequest.getPassword()));
        userMapper.updatePassword(user, passwordUpdateRequest);
        return userMapper.toUserResponse(userRepository.save(user))!=null;
    }

    public UserResponse getUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOTEXISTED));

        return userMapper.toUserResponse(user);
    }
    public UserResponse getUserInfoWithUserName(String username){
        User user = userRepository.findByUsername(username).orElseThrow(()->
                new AppException(ErrorCode.USER_NOTEXISTED));
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUserInfo(UserUpdateRequest userUpdateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOTEXISTED));
        userMapper.updateUser(user, userUpdateRequest);

        return userMapper.toUserResponse(userRepository.save(user));
    }

}
