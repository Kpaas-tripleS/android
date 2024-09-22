package com.example.android.friend.dto.response;

import com.example.android.friend.dto.request.BeFriendRequest;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class BeFriendResponseList {
    @SerializedName("beFriendResponseList")
    private List<BeFriendRequest> requests;

    public List<BeFriendRequest> getRequests() {
        return requests;
    }
}
