package com.example.android.friend.dto.request;

public class BeFriendRequest {
    private Long receiverId;

    public BeFriendRequest(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}