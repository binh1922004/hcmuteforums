package com.example.hcmuteforums.model.modelAdapter;

import com.example.hcmuteforums.model.dto.UserGeneral;

public class User {
    private String username;
    private String displayName;
    private String avatarUrl;

    public User(String username, String displayName, String avatarUrl) {
        this.username = username;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    // Chuyển đổi từ UserGeneral sang User
    public static User fromUserGeneral(UserGeneral userGeneral) {
        return new User(
                userGeneral.getUsername(),
                userGeneral.getFullName(),
                userGeneral.getAvt()
        );
    }
}
