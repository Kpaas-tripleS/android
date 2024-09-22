package com.example.android.friend.dto.request;

public class FriendRequestHandleRequest {
    Long requesterId;
    Boolean isAccepted;

    public FriendRequestHandleRequest(Long requesterId, Boolean isAccepted) {
        this.requesterId = requesterId;
        this.isAccepted = isAccepted;
    }
}
