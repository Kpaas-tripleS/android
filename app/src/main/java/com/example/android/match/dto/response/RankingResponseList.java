package com.example.android.match.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RankingResponseList {

    @SerializedName("rankingResponseList")
    private List<UserResponse> rankingResponseList;

    public List<UserResponse> getRankingResponseList() {
        return rankingResponseList;
    }
}

