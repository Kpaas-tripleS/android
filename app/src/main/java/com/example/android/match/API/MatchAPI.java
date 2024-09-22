package com.example.android.match.API;

import com.example.android.global.dto.response.ResponseTemplate;
import com.example.android.match.dto.request.MatchRequest;
import com.example.android.match.dto.response.MatchResponseList;
import com.example.android.match.dto.response.MatchResultResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MatchAPI {

    @GET("/matches/friend")
    Call<ResponseTemplate<MatchResponseList>> getMatchList();

    @POST("/matches")
    Call<Long> findMatch(@Body MatchRequest matchRequest);

    @POST("matches/{matchId}/accept")
    Call<ResponseTemplate<Void>> acceptMatch(@Path("matchId") Long matchId);

    @POST("matches/{matchId}/reject")
    Call<ResponseTemplate<Void>> rejectMatch(@Path("matchId") Long matchId);

    @DELETE("/matches/{matchId}")
    Call<ResponseTemplate<Void>> deleteMatch(@Path("matchId") Long matchId);

    @POST("/matches/{matchId}/quiz/{quizId}/answer")
    Call<ResponseTemplate<Void>> checkQuizForMatch(@Path("matchId") Long matchId, @Path("quizId") Long quizId, @Body String userAnswer);

    @GET("/matches/{matchId}/result")
    Call<ResponseTemplate<MatchResultResponse>> resultQuizForMatch(@Path("matchId") Long matchId);

}
