package com.example.android.user.dto.response;

import com.google.gson.annotations.SerializedName;

public class FindUserResponse {

    @SerializedName("userId")
    private Long userId;

    @SerializedName("nickname")
    private String nickname;

    public FindUserResponse(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public Long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
