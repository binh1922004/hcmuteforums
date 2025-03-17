package com.backend.backend.dto.response;

import com.backend.backend.entity.UserGeneral;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  TopicDetailResponse {
    UserGeneral userGeneral;
    String title;
    String content;
    Date createdAt;
    int likeCount;
    int replyCount;
    boolean isLiked;
    boolean isOwner;
}
