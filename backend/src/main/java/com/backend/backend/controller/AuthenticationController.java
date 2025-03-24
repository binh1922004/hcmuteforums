package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.request.AuthenticationRequest;
import com.backend.backend.dto.response.AuthenticationResponse;
import com.backend.backend.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(authenticationRequest))
                .build();
    }
    @PostMapping("/introspect")
    public ApiResponse<Boolean> introspect(@RequestParam String token) {
        return ApiResponse.<Boolean>builder()
                .result(authenticationService.introspect(token))
                .build();
    }
}
