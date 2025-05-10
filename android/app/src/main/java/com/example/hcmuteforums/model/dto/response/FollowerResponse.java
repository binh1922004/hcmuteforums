package com.example.hcmuteforums.model.dto.response;

import com.example.hcmuteforums.model.dto.UserGeneral;

public class FollowerResponse {
    String followId;
    UserGeneral userGeneral;

    public FollowerResponse(String followId, UserGeneral userGeneral) {
        this.followId = followId;
        this.userGeneral = userGeneral;
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
