package com.example.android.friend.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;
public class BeFriendResponseList {
    @SerializedName("beFriendResponseList")
    private List<BeFriendResponse> requests;

    public List<BeFriendResponse> getRequests() {
        return requests;
    }
}
