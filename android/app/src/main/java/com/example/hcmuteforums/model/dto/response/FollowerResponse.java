package com.example.hcmuteforums.model.dto.response;

import com.example.hcmuteforums.model.dto.UserGeneral;

public class FollowerResponse {
    String followId;
    Boolean hasFollowed;
    Boolean currentMe;

    UserGeneral userGeneral;

    public FollowerResponse(String followId, Boolean hasFollowed, UserGeneral userGeneral, Boolean me) {
        this.followId = followId;
        this.hasFollowed = hasFollowed;
        this.userGeneral = userGeneral;
        this.currentMe = me;
    }

    public Boolean getCurrentMe() {
        return currentMe;
    }

    public void setCurrentMe(Boolean currentMe) {
        this.currentMe = currentMe;
    }

    public Boolean getHasFollowed() {
        return hasFollowed;
    }

    public void setHasFollowed(Boolean hasFollowed) {
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
