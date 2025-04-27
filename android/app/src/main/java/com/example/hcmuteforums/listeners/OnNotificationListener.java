package com.example.hcmuteforums.listeners;

import com.example.hcmuteforums.model.dto.NotificationDTO;

public interface OnNotificationListener {
    void onNewNotification(NotificationDTO notificationData);
    void onNotificationStatusChanged(boolean hasUnread);
}
