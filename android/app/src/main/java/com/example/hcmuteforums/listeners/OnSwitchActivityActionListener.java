package com.example.hcmuteforums.listeners;

public interface OnSwitchActivityActionListener {
    void onClickProfile(String username);
    void onClickTopicDetail(String topicId, boolean isOwner);
}
