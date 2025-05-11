package com.example.hcmuteforums.model.dto.response;

public class FollowStatusResponse {
    private boolean isFollowing;

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public FollowStatusResponse(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }
}
