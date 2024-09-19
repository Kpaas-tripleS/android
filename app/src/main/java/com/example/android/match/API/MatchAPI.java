package com.example.android.match.API;

import com.example.android.global.dto.response.ResponseTemplate;
import com.example.android.match.dto.request.MatchRequest;
import com.example.android.match.dto.response.MatchResponseList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MatchAPI {

    @GET("/matches/friend")
    Call<ResponseTemplate<MatchResponseList>> getMatchList();

    //@POST("/matches")
    //Call<Long> findMatch(@Body MatchRequest matchRequest);

    //@GET("/matches/{matchId}/status")
    //Call<ResponseTemplate<Void>> matchStatus(@Path("matchId") Long matchId);

}
