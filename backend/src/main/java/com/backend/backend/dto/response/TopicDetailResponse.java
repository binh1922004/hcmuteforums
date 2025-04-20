package com.backend.backend.dto.response;

import com.backend.backend.dto.UserGeneral;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  TopicDetailResponse {
    String id;
    UserGeneral userGeneral;
    String title;
    String content;
    Date createdAt;
    int likeCount;
    int replyCount;
    boolean isLiked;
    boolean isOwner;
    List<String> imgUrls;
}
