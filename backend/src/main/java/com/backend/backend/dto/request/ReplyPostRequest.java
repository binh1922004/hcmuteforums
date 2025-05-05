package com.backend.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyPostRequest {
    String content;
    String parentReplyId;
    String targetUserName;
    String topicId;
}
