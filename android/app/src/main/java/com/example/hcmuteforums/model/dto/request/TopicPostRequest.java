package com.example.hcmuteforums.model.dto.request;

public class TopicPostRequest {
    String title;
    String content;
    String subCategoryId;

    public TopicPostRequest(String title, String content, String subCategoryId) {
        this.title = title;
        this.content = content;
        this.subCategoryId = subCategoryId;
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

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
}
