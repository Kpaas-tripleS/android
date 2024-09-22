package com.example.android.friend.api;

import com.example.android.friend.dto.response.FriendResponseList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FriendApi {
    @GET("/friends")
    Call<FriendResponseList> getFriends();

    @DELETE("/friends")
    Call<Void> deleteFriend(@Query("friendId") Long friendId);
}
