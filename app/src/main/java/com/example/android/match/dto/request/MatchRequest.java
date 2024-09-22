package com.example.android.match.dto.request;
import com.google.gson.annotations.SerializedName;

public class MatchRequest {

    @SerializedName("creatTime")
    private String creatTime;

    public MatchRequest(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }
}
