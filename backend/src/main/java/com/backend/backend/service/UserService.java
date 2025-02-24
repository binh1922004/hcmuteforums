package com.backend.backend.service;

import com.backend.backend.dto.request.UserCreationRequest;
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
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    public boolean createUser(UserCreationRequest userCreationRequest) {
        if (userRepository.existsUserByUsername(userCreationRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        userCreationRequest.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        userRepository.save(userMapper.toUser(userCreationRequest));

        return true;
    }

    public UserResponse getUserInfo(){
        System.out.println("GO");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOTEXISTED));

        return userMapper.toUserResponse(user);
    }


}
