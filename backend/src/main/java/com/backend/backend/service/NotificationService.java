package com.backend.backend.service;


import com.backend.backend.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotificationToUser(String userId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
    }
    //send with structure
    public void sendStructuredNotificationToUser(String userId, NotificationDTO notification) {
        // Đảm bảo notification có ID nếu chưa có
        if (notification.getId() == null) {
            notification.setId(UUID.randomUUID().toString());
        }

        // Đảm bảo ngày tạo được set
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(new Date());
        }

        // Gửi object notification đến client
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
    }

    public NotificationDTO createReplyNotification(String targetUserId, String topicId, String replyId, String senderName) {
        return NotificationDTO.builder()
                .id(UUID.randomUUID().toString())
                .userId(targetUserId)
                .type("REPLY")
                .content("💬 " + senderName + " đã bình luận bài viết của bạn!")
                .topicId(topicId)
                .replyId(replyId)
                .senderName(senderName)
                .isRead(false)
                .createdAt(new Date())
                .build();
    }
}

