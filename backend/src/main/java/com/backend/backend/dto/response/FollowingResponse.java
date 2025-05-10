package com.backend.backend.dto.response;

import com.backend.backend.dto.UserGeneral;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowingResponse {
    String followId;
    UserGeneral userGeneral;
}
