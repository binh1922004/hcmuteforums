package com.example.hcmuteforums.listeners;

public interface OnMenuActionListener {
    public void onUpdate(String replyId, String content, int pos);
    public void onDelete(String replyId, int pos);
}
