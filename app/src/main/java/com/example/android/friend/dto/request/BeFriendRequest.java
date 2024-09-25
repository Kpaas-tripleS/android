package com.example.android.friend.dto.request;

import com.google.gson.annotations.SerializedName;

public class BeFriendRequest {
    private Long receiverId;

    @SerializedName("requesterId")
    private Long requesterId;

    @SerializedName("isAccepted")
    private boolean isAccepted;

//    @SerializedName("nickname")
    private String nickname;

    public String getNickname() {
        return nickname;
    }
public void setNickname(String nickname) {
    this.nickname = nickname;
}

    public BeFriendRequest(String nickname) {
        this.nickname = nickname;
    }


    public BeFriendRequest(Long receiverId, Long requesterId, boolean isAccepted) {
        this.receiverId = receiverId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
    }