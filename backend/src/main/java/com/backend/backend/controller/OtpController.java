package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.request.OtpRequest;
import com.backend.backend.dto.request.OtpValidateRequest;
import com.backend.backend.service.OtpService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/otp")
public class OtpController {
    OtpService otpService;

    @PostMapping("/get")
    public ApiResponse<Boolean> sendOtp(@RequestBody OtpRequest otpRequest) {
        return ApiResponse.<Boolean>builder()
                .result(otpService.sendOtp(otpRequest))
                .build();
    }
    @PostMapping("/validate")
    public ApiResponse<Boolean> validateOtp(@RequestBody OtpValidateRequest otpValidateRequest) {
        return ApiResponse.<Boolean>builder()
                .result(otpService.validateOtp(otpValidateRequest))
                .build();
    }
}
