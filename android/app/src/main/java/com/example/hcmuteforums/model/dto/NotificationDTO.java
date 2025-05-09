package com.example.hcmuteforums.model.dto;

import java.util.Date;

public class NotificationDTO {
    private String id;           // ID thông báo
    private String userId;       // ID người nhận
    private String senderId;     // ID người gửi (nếu có)
    private String senderName;   // Tên người gửi
    private String type;         // Loại thông báo: "REPLY", "LIKE", "SYSTEM", etc.
    private String content;      // Nội dung thông báo
    private String topicId;      // ID của bài viết liên quan
    private String replyId;      // ID của reply liên quan
    private boolean isRead;      // Trạng thái đã đọc hay chưa
    private Date createdAt;      // Thời điểm tạo thông báo
    private String sendUserAvatar;

    public String getSendUserAvatar() {
        return sendUserAvatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
