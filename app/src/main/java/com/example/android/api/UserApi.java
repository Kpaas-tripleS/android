package com.example.android.api;

import com.example.android.dto.request.LoginRequest;
import com.example.android.dto.request.SignUpRequest;
import com.example.android.dto.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {

    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/auth/sign-up")
    Call<Void> signUp(@Body SignUpRequest signUpRequest);
}
