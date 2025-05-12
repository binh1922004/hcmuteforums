package com.example.hcmuteforums.model.dto.response;

import com.example.hcmuteforums.model.dto.UserGeneral;

public class FollowingResponse {
    String followId;
    boolean hasFollowed;
    UserGeneral userGeneral;

    public FollowingResponse(String followId, boolean hasFollowed, UserGeneral userGeneral) {
        this.followId = followId;
        this.hasFollowed = hasFollowed;
        this.userGeneral = userGeneral;
    }

    public boolean isHasFollowed() {
        return hasFollowed;
    }

    public void setHasFollowed(boolean hasFollowed) {
        this.hasFollowed = hasFollowed;
    }

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public UserGeneral getUserGeneral() {
        return userGeneral;
    }

    public void setUserGeneral(UserGeneral userGeneral) {
        this.userGeneral = userGeneral;
    }
}
