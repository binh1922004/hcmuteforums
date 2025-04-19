package com.example.hcmuteforums.model.dto.response;

import com.example.hcmuteforums.model.dto.UserGeneral;

import java.util.Date;
import java.util.List;

public class TopicDetailResponse {
    private String id;
    private UserGeneral userGeneral;
    private String title;
    private String content;
    private Date createdAt;
    private int likeCount;
    private int replyCount;
    private boolean liked;
    private boolean owner;
    private List<String> imgUrls;

    public UserGeneral getUserGeneral() {
        return userGeneral;
    }

    public void setUserGeneral(UserGeneral userGeneral) {
        this.userGeneral = userGeneral;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }
}
