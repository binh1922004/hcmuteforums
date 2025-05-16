package com.backend.backend.service;


import com.backend.backend.dto.NotificationDTO;
import com.backend.backend.dto.response.PageResponse;
import com.backend.backend.entity.Notification;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.repository.UserRepository;
import com.backend.backend.utils.Constant;
import com.backend.backend.utils.NotificationContent;
import com.backend.backend.entity.Topic;
import com.backend.backend.entity.User;
import com.backend.backend.repository.NotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationService {
    SimpMessagingTemplate messagingTemplate;
    //repo
    NotificationRepository notificationRepository;
    UserRepository userRepository;

    public void sendNotificationToUser(String userId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
    }
    //send with structure
    public void sendStructuredNotificationToUser(Notification notification) {
        var savedNotification = notificationRepository.save(notification);
        // Gửi object notification đến client
        messagingTemplate.convertAndSend("/topic/notifications/" + savedNotification.getRecieveUser().getId(), convertToNotificationDTO(savedNotification));
    }

    public void sendStructuredNotificationToUserFollow(Notification notification) {
        var savedNotification = notificationRepository.save(notification);
        // Gửi object notification đến client
        messagingTemplate.convertAndSend("/topic/notifications/" + savedNotification.getRecieveUser().getId(), convertToNotificationDTO(savedNotification));
    }

    public NotificationDTO convertToNotificationDTO(Notification notification) {
        return NotificationDTO.builder()
                .sendUserAvatar(Constant.url + notification.getSendUser().getProfile().getAvatarUrl())
                .id(notification.getId())
                .receivedUser(notification.getRecieveUser().getFullName()) //receivedUser is user who receive notificaion
                .type(notification.getContent().name())
                .content(notification.getContent().getContent())
                .topicId(notification.getTopic() != null ?  notification.getTopic().getId() : "")
                .senderName(notification.getSendUser().getUsername())
                .actionId(notification.getActionId())
                .isRead(false)
                .createdAt(notification.getCreatedAt())
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

    public Boolean deleteNotification(String actionId) {
        Optional<Notification> notification = notificationRepository.findNotificationByActionId(actionId);
        notification.ifPresent(notificationRepository::delete);
        return true;
    }
    public PageResponse<NotificationDTO> getNotifications(
            int page,
            int size,
            String sortBy,
            String direction){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOTEXISTED));
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Notification> notifications = notificationRepository.findALlByRecieveUser(user, pageable);
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification : notifications.getContent()) {
            notificationDTOList.add(convertToNotificationDTO(notification));
        }
        return PageResponse.<NotificationDTO>builder()
                .content(notificationDTOList)
                .pageNumber(notifications.getNumber())
                .pageSize(notifications.getSize())
                .totalElements(notifications.getTotalElements())
                .totalPages(notifications.getTotalPages())
                .last(notifications.isLast())
                .build();
    }
}

