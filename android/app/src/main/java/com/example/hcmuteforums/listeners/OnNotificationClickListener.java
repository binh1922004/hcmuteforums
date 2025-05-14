package com.example.hcmuteforums.listeners;

public interface OnNotificationClickListener {
    void onClickLike(String topicId);
    void onClickReply(String topicId, String replyId);
}
