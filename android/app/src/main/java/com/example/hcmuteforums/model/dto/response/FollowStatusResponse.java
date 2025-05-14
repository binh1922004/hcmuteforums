package com.example.hcmuteforums.model.dto.response;

public class FollowStatusResponse {
    private boolean following;

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public FollowStatusResponse(boolean following) {
        this.following = following;
    }
}
