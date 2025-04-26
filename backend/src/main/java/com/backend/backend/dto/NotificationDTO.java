package com.backend.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    String id;           // ID thông báo
    String userId;       // ID người nhận
    String senderId;     // ID người gửi (nếu có)
    String senderName;   // Tên người gửi
    String type;         // Loại thông báo: "REPLY", "LIKE", "SYSTEM", etc.
    String content;      // Nội dung thông báo
    String topicId;      // ID của bài viết liên quan
    String replyId;      // ID của reply liên quan
    boolean isRead;      // Trạng thái đã đọc hay chưa
    Date createdAt;      // Thời điểm tạo thông báo
}
