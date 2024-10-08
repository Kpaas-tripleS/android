package com.example.android.friend.dto.response;

import com.google.gson.annotations.SerializedName;

public class FriendResponse {
    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    public FriendResponse(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
