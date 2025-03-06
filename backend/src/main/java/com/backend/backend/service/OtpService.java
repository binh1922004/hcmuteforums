package com.backend.backend.service;

import com.backend.backend.dto.EmailDTO;
import com.backend.backend.dto.request.OtpRequest;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpService {
    OtpGenerator otpGenerator;
    EmailService emailService;
    UserRepository userRepository;

    public Boolean sendOtp(OtpRequest request) {
        var email = request.getEmail();
        var username = request.getUsername();

        if (userRepository.existsUserByUsername(username))
            throw new AppException(ErrorCode.USER_EXISTED);

        if (userRepository.existsUserByEmail(email))
            throw new AppException(ErrorCode.EMAIL_EXISTED);



        String otp = otpGenerator.generateOtp(email);
        String htmlContent = "<div style='text-align: center; font-family: Arial, sans-serif;'>" +
                "<h2 style='color: #333;'>Mã OTP của bạn</h2>" +
                "<p style='font-size: 20px; font-weight: bold; color: #007bff;'>" + otp + "</p>" +
                "<p>Vui lòng không chia sẻ mã này với bất kỳ ai.</p>" +
                "<br/>" +
                "<p style='font-size: 14px; color: #888;'>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>" +
                "</div>";
        List<String> recipients = new ArrayList<>();
        recipients.add(email);
        EmailDTO emailDTO = EmailDTO.builder()
                .recipients(recipients)
                .body(htmlContent)
                .subject("Mã OTP đăng ký tài khoản.")
                .build();
        return emailService.sendSimpleMessage(emailDTO);
    }

    public Boolean validateOtp(String email, String otp) {
        return otpGenerator.verifyOtp(email, otp);
    }
}
