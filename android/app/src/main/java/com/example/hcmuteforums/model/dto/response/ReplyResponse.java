package com.example.hcmuteforums.model.dto.response;

import com.example.hcmuteforums.model.dto.UserGeneral;

import java.util.List;

public class ReplyResponse {
    private String id;
    private String content;
    private String parentReplyId;
    private UserGeneral userGeneral;
    private boolean hasChild;
    private boolean isShowChild;
    private boolean isLast;

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public boolean isShowChild() {
        return isShowChild;
    }

    public void setShowChild(boolean showChild) {
        isShowChild = showChild;
    }

    private boolean isExpanded = false;

    // Getter và Setter
    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
    private List<ReplyResponse> listChild;
    public boolean isHasChild() {
        return hasChild;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getParentReplyId() {
        return parentReplyId;
    }

    public UserGeneral getUserGeneral() {
        return userGeneral;
    }

    public List<ReplyResponse> getListChild() {
        return listChild;
    }

    public void setListChild(List<ReplyResponse> listChild) {
        this.listChild = listChild;
    }
}
