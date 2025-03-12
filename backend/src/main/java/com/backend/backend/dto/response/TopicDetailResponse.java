package com.backend.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicDetailResponse {
    String fullName;
    String title;
    String description;
    Date createdAt;
    int likeCount;
    int replyCount;
    boolean isLiked;
    boolean isYours;
}
