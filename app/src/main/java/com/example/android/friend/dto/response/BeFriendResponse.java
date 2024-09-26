package com.example.android.friend.dto.response;

import com.google.gson.annotations.SerializedName;

public class BeFriendResponse {

    @SerializedName("requesterId")
    private Long requesterId;

    @SerializedName("nickname")
    private String nickname;

    public BeFriendResponse(Long requesterId, String nickname) {
        this.requesterId = requesterId;
        this.nickname = nickname;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
