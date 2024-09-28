package com.example.android.user.dto.request;

import com.google.gson.annotations.SerializedName;

public class SignUpRequest {

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("phone")
    private String phone;

    public SignUpRequest(String name, String email, String password, String nickname, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }
}
