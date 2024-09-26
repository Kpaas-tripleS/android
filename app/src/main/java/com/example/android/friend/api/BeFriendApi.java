package com.example.android.friend.api;

import com.example.android.friend.dto.request.BeFriendRequest;
import com.example.android.friend.dto.response.BeFriendResponseList;
import com.example.android.global.ResponseTemplate;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BeFriendApi {
    @POST("/be-friends/request")
    Call<Void> sendFriendRequest(@Body BeFriendRequest beFriendRequest);

    @GET("/be-friends")
    Call<ResponseTemplate<BeFriendResponseList>> getFriendRequestList(@Query("userId") Long userId);

    @POST("/be-friends")
    Call<ResponseTemplate<BeFriendRequest>> handleFriendRequest(@Body BeFriendRequest beFriendRequest);
}
