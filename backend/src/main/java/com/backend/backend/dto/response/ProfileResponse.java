package com.backend.backend.dto.response;

import com.backend.backend.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {
    String bio;
    String avatarUrl;
    String coverUrl;
}
