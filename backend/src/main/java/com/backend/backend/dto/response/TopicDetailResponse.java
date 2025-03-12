package com.backend.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicDetailResponse {
    String fullName;
    String title;
    String description;
    int likeCount;
    int commentCount;
}
