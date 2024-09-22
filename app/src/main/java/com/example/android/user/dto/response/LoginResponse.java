package com.example.android.user.dto.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("accessToken")
    private final String accessToken;

    @SerializedName("refreshToken")
    private final String refreshToken;

    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public static LoginResponse of(String accessToken, String refreshToken) {
        return new LoginResponse(accessToken, refreshToken);
    }
}
