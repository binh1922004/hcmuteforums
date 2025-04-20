package com.backend.backend.dto.response;

import com.backend.backend.dto.UserGeneral;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReplyResponse {
    String id;
    String content;
    String parentReplyId;
    UserGeneral userGeneral;
}
