package com.example.hcmuteforums.model.dto.request;

public class FollowRequest {
    private String followedUsername;

    public FollowRequest(String followedUsername) {
        this.followedUsername = followedUsername;
    }

    public String getFollowedUsername() {
        return followedUsername;
    }

    public void setFollowedUsername(String followedUsername) {
        this.followedUsername = followedUsername;
    }
}
