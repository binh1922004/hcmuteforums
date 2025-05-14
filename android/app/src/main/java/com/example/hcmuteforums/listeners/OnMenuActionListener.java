package com.example.hcmuteforums.listeners;

public interface OnMenuActionListener {
    void onUpdate(String id, String content, int pos);
    void onDelete(String id, int pos);
    void onCopy(String content);
}
