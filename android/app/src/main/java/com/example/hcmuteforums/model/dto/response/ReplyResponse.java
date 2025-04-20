package com.example.hcmuteforums.model.dto.response;

import com.example.hcmuteforums.model.dto.UserGeneral;

public class ReplyResponse {
    private String id;
    private String content;
    private String parentReplyId;
    private UserGeneral userGeneral;

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getParentReplyId() {
        return parentReplyId;
    }

    public UserGeneral getUserGeneral() {
        return userGeneral;
    }
}
