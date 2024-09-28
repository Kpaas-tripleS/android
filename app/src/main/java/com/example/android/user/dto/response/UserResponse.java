package com.example.android.user.dto.response;

import com.example.android.match.dto.response.Grade;
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

    @SerializedName("wine_count")
    private Long win_count;

    @SerializedName("grade")
    private Grade grade;

    public UserResponse(Long id, String name, String nickname, String profile_image, Long win_count, Grade grade) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.profile_image = profile_image;
        this.win_count = win_count;
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public Long getWin_count() {
        return win_count;
    }

    public void setWin_count(Long win_count) {
        this.win_count = win_count;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
