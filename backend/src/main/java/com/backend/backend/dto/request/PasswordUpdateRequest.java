package com.backend.backend.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordUpdateRequest {
    private String email;
    private String password;
    private String otp;
    private String username;
}
