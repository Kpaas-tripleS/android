package com.example.android.friend.api;

import com.example.android.friend.dto.request.BeFriendRequest;
import com.example.android.friend.dto.response.BeFriendResponseList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BeFriendApi {
    @POST("/be-friends/request")
    Call<Void> sendFriendRequest(@Body BeFriendRequest beFriendRequest);

    @GET("/be-friends")
    Call<BeFriendResponseList> getFriendRequestList();

    @POST("/be-friends")
    Call<Void> handleFriendRequest(@Body BeFriendRequest beFriendRequest);
}
