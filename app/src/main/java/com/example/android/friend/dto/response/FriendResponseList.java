package com.example.android.friend.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendResponseList {
    @SerializedName("friendResponseList")
    private List<FriendResponse> requests;

    public List<FriendResponse> getRequests() {
        return requests;
    }

}
