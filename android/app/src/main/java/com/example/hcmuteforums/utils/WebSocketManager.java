package com.example.hcmuteforums.utils;

import android.content.Context;
import android.util.Log;

import com.example.hcmuteforums.listeners.OnNotificationListener;
import com.example.hcmuteforums.model.dto.NotificationDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.dto.StompHeader;

public class WebSocketManager {
    private static final String TAG = "WebSocketManager";
    private static WebSocketManager instance;
    private StompClient stompClient;
    private Disposable topicSubscription;
    private List<OnNotificationListener> listeners = new ArrayList<>();
    private List<NotificationDTO> unreadNotifications = new ArrayList<>(); // Lưu danh sách thông báo chưa đọc
    private boolean hasUnreadNotifications = false;
    private String userId;

    // Singleton pattern
    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    // Kết nối tới WebSocket server
    public void connect(Context context, String authToken, String userId) {
        this.userId = userId;

        // Đóng kết nối cũ nếu có
        disconnect();

        // URL của WebSocket server
        String url = "wss://ball.io.vn/ws";

        // Headers cho STOMP
        List<StompHeader> headers = new ArrayList<>();
//        headers.add(new StompHeader("Authorization", "Bearer " + authToken));

        // Tạo STOMP client
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);

        // Cấu hình logging (tùy chọn)
        stompClient.withClientHeartbeat(30000).withServerHeartbeat(30000);

        // Kết nối
        stompClient.connect(headers);

        subcribeToTopic(stompClient);
    }

    private void subcribeToTopic(StompClient stompClient) {
        // Đăng ký nhận thông báo khi có tin nhắn mới
        topicSubscription = stompClient.topic("/topic/notifications/" + userId)
                .subscribe(topicMessage -> {
                    Log.d(TAG, "Received notification: " + topicMessage.getPayload());

                    // Parse JSON payload thành object
                    NotificationDTO notificationData = new Gson().fromJson(
                            topicMessage.getPayload(),
                            NotificationDTO.class);

                    // Thêm thông báo vào danh sách
                    unreadNotifications.add(notificationData);

                    // Thông báo cho các listener
                    for (OnNotificationListener listener : listeners) {
                        listener.onNewNotification(notificationData);
                        listener.onNotificationStatusChanged(!unreadNotifications.isEmpty());
                    }
                }, throwable -> {
                    Log.e(TAG, "Error on subscribe topic", throwable);
                });
    }

    // Ngắt kết nối WebSocket
    public void disconnect() {
        if (topicSubscription != null && !topicSubscription.isDisposed()) {
            topicSubscription.dispose();
        }
        if (stompClient != null) {
            stompClient.disconnect();
        }
    }

    // Thêm listener
    public void addNotificationListener(OnNotificationListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            // Gửi trạng thái hiện tại cho listener mới
            listener.onNotificationStatusChanged(hasUnreadNotifications);
        }
    }

    // Xóa listener
    public void removeNotificationListener(OnNotificationListener listener) {
        listeners.remove(listener);
    }

    // Đánh dấu đã đọc tất cả thông báo
    public void markAllNotificationsAsRead() {
        unreadNotifications.clear();
        for (OnNotificationListener listener : listeners) {
            listener.onNotificationStatusChanged(hasUnreadNotifications);
        }
    }

    // Kiểm tra có thông báo chưa đọc không
    public int getUnreadNotificationCount() {
        return unreadNotifications.size();
    }

    public boolean hasUnreadNotifications() {
        return !unreadNotifications.isEmpty();
    }


}