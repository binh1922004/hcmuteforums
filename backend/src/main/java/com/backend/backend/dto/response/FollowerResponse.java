package com.backend.backend.dto.response;

import com.backend.backend.dto.UserGeneral;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowerResponse {
    String followId;
    boolean hasFollowed;
    boolean currentMe;
    UserGeneral userGeneral;
}
