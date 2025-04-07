package com.backend.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String username;
    String fullName;
    String phone;
    LocalDate dob;
    String address;
    String gender;
    String email;
}
