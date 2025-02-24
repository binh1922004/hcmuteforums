package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.request.UserCreationRequest;
import com.backend.backend.dto.response.UserResponse;
import com.backend.backend.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ApiResponse<Boolean> createUser(@RequestBody UserCreationRequest userCreationRequest) {
        return ApiResponse.<Boolean>builder()
                .result(userService.createUser(userCreationRequest))
                .build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> myInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserInfo())
                .build();
    }
}
