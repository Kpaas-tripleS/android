package com.example.android.user.api;

import com.example.android.global.ResponseTemplate;
import com.example.android.user.dto.request.LoginRequest;
import com.example.android.user.dto.request.SignUpRequest;
import com.example.android.user.dto.response.FindUserResponse;
import com.example.android.user.dto.response.FindUserResponseList;
import com.example.android.user.dto.response.LoginResponse;
import com.kakao.sdk.user.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {
    @GET("/auth/findUser")
    Call<ResponseTemplate<FindUserResponseList>> findUser();

    @POST("/auth/login")
    Call<ResponseTemplate<LoginResponse>> login(@Body LoginRequest loginRequest);

    @POST("/auth/kakao-login")
    Call<ResponseTemplate<LoginResponse>> kakaoLogin(@Query("token") String token);

    @POST("/auth/sign-up")
    Call<Void> signUp(@Body SignUpRequest signUpRequest);

    @GET("/accessToken")
    Call<UserResponse> getAccessToken(@Header("Authorization") String token);
}