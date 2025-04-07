package com.backend.backend.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String username;
    String password;
    String email;
    String fullName;
    LocalDate dob;
    String otp;
    String gender;
}
