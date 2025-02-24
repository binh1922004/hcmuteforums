package com.backend.backend.service;

import com.backend.backend.dto.request.UserCreationRequest;
import com.backend.backend.mapper.UserMapper;
import com.backend.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
            return false;
        }
        userCreationRequest.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        userRepository.save(userMapper.toUser(userCreationRequest));

        return true;
    }

}
