package com.example.hcmuteforums.model.dto.request;

import com.example.hcmuteforums.model.dto.UserGeneral;

public class ReplyRequest {
    private String content;
    private String parentReplyId;
    private UserGeneral userGeneral;


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
