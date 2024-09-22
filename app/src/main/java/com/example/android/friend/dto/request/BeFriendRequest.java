package com.example.android.friend.dto.request;

import com.google.gson.annotations.SerializedName;

public class BeFriendRequest {

    @SerializedName("requesterId")
    private Long requesterId;

    @SerializedName("nickname")
    private String nickname;

    public BeFriendRequest(Long requesterId, String nickname) {
        this.requesterId = requesterId;
        this.nickname = nickname;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
