package com.example.hcmuteforums.model.dto.request;

public class ProfileUpdateRequest {
    String bio;
    String avatarUrl;
    String coverUrl;

    public ProfileUpdateRequest(String bio, String avatarUrl, String coverUrl) {
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.coverUrl = coverUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
