package com.example.hcmuteforums.model.dto.request;

public class ReplyPostRequest {
    private String content;
    private String parentReplyId;
    private String targetUserName;
    private String topicId;

    public ReplyPostRequest(String content, String parentReplyId, String targetUserName, String topicId) {
        this.content = content;
        this.parentReplyId = parentReplyId;
        this.targetUserName = targetUserName;
        this.topicId = topicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParentReplyId() {
        return parentReplyId;
    }

    public void setParentReplyId(String parentReplyId) {
        this.parentReplyId = parentReplyId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}
