package com.example.android.friend.dto.request;

import com.google.gson.annotations.SerializedName;

public class BeFriendRequest {

    @SerializedName("receiverId")
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

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setNickname(Long requesterId) {
        this.requesterId = requesterId;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

}
