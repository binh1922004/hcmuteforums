package com.example.hcmuteforums.listeners;

import com.example.hcmuteforums.model.dto.response.ReplyResponse;

public interface OnReplyClickListener {
    void onReplyClick(ReplyResponse reply);
    // to fetch api
    void onShowChildReply(ReplyResponse reply);
    void onHideChildReply(ReplyResponse reply);
}