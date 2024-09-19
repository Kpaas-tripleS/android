package com.example.android.match.API;

import com.example.android.global.dto.response.ResponseTemplate;
import com.example.android.match.dto.response.RankingResponseList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RankingAPI {

    @GET("/matches/rank/all")
    Call<ResponseTemplate<RankingResponseList>> getAllRanking();

    @GET("/matches/rank/friend")
    Call<ResponseTemplate<RankingResponseList>> getFriendRanking();

}
