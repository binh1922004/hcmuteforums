package com.example.hcmuteforums.model.dto.request;

public class ProfileUpdateRequest {
    String bio;


    public ProfileUpdateRequest(String bio) {
        this.bio = bio;

    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
