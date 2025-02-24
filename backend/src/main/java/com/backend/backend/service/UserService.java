package com.backend.backend.service;

import com.backend.backend.dto.request.UserCreationRequest;
import com.backend.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;

    public boolean createUser(UserCreationRequest userCreationRequest) {
        if (userRepository.existsUserByUsername(userCreationRequest.getUsername())) {
            return false;
        }

    }

}
