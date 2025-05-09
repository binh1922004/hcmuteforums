package com.example.hcmuteforums.model.dto.response;

import java.util.Date;

public class FollowResponse {
    private String followerId;
    private String followedId;
    private String status;
    private Date createdAt;

    public FollowResponse(String followerId, String followedId, String status, Date createdAt) {
        this.followerId = followerId;
        this.followedId = followedId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getFollowedId() {
        return followedId;
    }

    public void setFollowedId(String followedId) {
        this.followedId = followedId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
