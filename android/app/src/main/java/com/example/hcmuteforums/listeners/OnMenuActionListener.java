package com.example.hcmuteforums.listeners;

public interface OnMenuActionListener {
    void onUpdate(String replyId, String content, int pos);
    void onDelete(String replyId, int pos);
    void onCopy(String content);
}
