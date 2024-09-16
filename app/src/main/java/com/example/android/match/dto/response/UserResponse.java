package com.example.android.match.dto.response;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("profile_image")
    private String profile_image;

    @SerializedName("win_count")
    private Long win_count;

    @SerializedName("grade")
    private Grade grade;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public Long getWin_count() {
        return win_count;
    }

    public Grade getGrade() {
        return grade;
    }
}

