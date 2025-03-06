package com.backend.backend.service;

import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpGenerator {
    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int THROTTLE_EXPIRY_MINUTES = 2;
    private static final String OTP_PREFIX = "otp:";
    private static final String THROTTLE_KEY_PREFIX = "throttle:";
    private static final String WAIT_REGISTER_PREFIX = "wait:";

    RedisTemplate<String, String> redisTemplate;
    Random random = new Random();
    public String generateOtp(String email) {

        if (redisTemplate.hasKey(THROTTLE_KEY_PREFIX + email)) {
            throw new AppException(ErrorCode.OTP_LIMIT_TIME);
        }

        String otp = String.valueOf(100000 + random.nextInt(900000)); // 6-digit OTP
        String key = OTP_PREFIX + email;
        String throttle_key = THROTTLE_KEY_PREFIX + email;
        //save otp to redis
        redisTemplate.opsForValue().set(key, otp, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(throttle_key, "true", THROTTLE_EXPIRY_MINUTES, TimeUnit.MINUTES);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        String key = OTP_PREFIX + email;
        String throttle_ey = THROTTLE_KEY_PREFIX + email;
        String storedOtp = redisTemplate.opsForValue().get(key);
        if (storedOtp != null && storedOtp.equals(otp)) {
            //delete old key
            redisTemplate.delete(key);
            if (redisTemplate.opsForValue().get(throttle_ey) != null)
                redisTemplate.delete(throttle_ey);

            return true;
        }
        return false;
    }


}
