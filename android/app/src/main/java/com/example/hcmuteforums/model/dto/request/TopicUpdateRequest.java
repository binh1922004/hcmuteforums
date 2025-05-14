package com.example.hcmuteforums.model.dto.request;

public class TopicUpdateRequest {
    private String content;
    private String title;

    public TopicUpdateRequest(String content, String title) {
        this.content = content;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
