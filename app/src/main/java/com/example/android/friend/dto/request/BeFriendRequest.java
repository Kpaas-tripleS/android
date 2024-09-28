package com.example.android.friend.dto.request;

import com.google.gson.annotations.SerializedName;

public class BeFriendRequest {
    @SerializedName("receiverId")
    private Long receiverId;

    @SerializedName("requesterId")
    private Long requesterId;

    @SerializedName("isAccepted")
    private boolean isAccepted;

    public BeFriendRequest(Long receiverId ,Long requesterId, boolean isAccepted) {
        this.receiverId = receiverId;
        this.requesterId = requesterId;
        this.isAccepted = isAccepted;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    @Override
    public String toString() {
        return "BeFriendRequest{" +
                "userId=" + receiverId +
                ", requesterId=" + requesterId +
                ", isAccepted=" + isAccepted +
                '}';
    }
}
