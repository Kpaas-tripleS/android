package com.example.android.global;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;

public class AuthUser {
    private Long userId;
    private List<String> roles;

    // 생성자
    public AuthUser(Long userId, List<String> roles) {
        this.userId = userId;
        this.roles = roles;
    }

    // getter 메서드
    public Long getUserId() {
        return userId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static AuthUser fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, AuthUser.class);
    }
}
