package com.backend.backend.utils;

import lombok.Getter;

@Getter
public enum NotificationContent {
    REPLY(" đã bình luận về bài viết của bạn "),
    LIKE(" đã thích bài viết của bạn"),
    TAG(" đã nhắc đến bạn trong một bình luận"),
    FOLLOW(" đã theo dõi bạn")
    ;
    NotificationContent(String t){
        content = t;
    }
    private String content;
}
