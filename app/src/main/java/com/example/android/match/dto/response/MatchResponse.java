package com.example.android.match.dto.response;

import com.google.gson.annotations.SerializedName;

public class MatchResponse {

    @SerializedName("matchId")
    private Long matchId;

    @SerializedName("leader")
    private UserResponse leader;

    @SerializedName("follower")
    private UserResponse follower;

    public Long getMatchId() {
        return matchId;
    }

    public UserResponse getLeader() {
        return leader;
    }

    public UserResponse getFollower() {
        return follower;
    }
}


