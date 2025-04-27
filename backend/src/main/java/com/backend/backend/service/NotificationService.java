package com.backend.backend.service;


import com.backend.backend.dto.NotificationDTO;
import com.backend.backend.entity.Notification;
import com.backend.backend.entity.NotificationContent;
import com.backend.backend.entity.Topic;
import com.backend.backend.entity.User;
import com.backend.backend.repository.NotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationService {
    SimpMessagingTemplate messagingTemplate;
    //repo
    NotificationRepository notificationRepository;

    public void sendNotificationToUser(String userId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
    }
    //send with structure
    public void sendStructuredNotificationToUser(Notification notificatio) {
        var savedNotification = notificationRepository.save(notificatio);
        // Gửi object notification đến client
        messagingTemplate.convertAndSend("/topic/notifications/" + savedNotification.getRecieveUser().getId(), convertToNotificationDTO(savedNotification));
    }

    public NotificationDTO convertToNotificationDTO(Notification notification) {
        return NotificationDTO.builder()
                .receivedUser(notification.getRecieveUser().getFullName()) //receivedUser is user who receive notificaion
                .type(notification.getContent().name())
                .content(notification.getContent().getContent())
                .topicId(notification.getTopic().getId())
                .senderName(notification.getSendUser().getFullName())
                .isRead(false)
                .createdAt(new Date())
                .build();
    }

    public Notification createNotification(String actionId, User sendUser, User receiverUser, NotificationContent notificationContent, Topic topic) {
        return Notification.builder()
                .actionId(actionId)
                .sendUser(sendUser)
                .recieveUser(receiverUser)
                .content(notificationContent)
                .topic(topic)
                .createdAt(new Date())
                .build();
    }
}

