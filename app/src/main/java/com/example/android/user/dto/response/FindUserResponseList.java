package com.example.android.user.dto.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FindUserResponseList {

    @SerializedName("findUserResponseList")
    private List<FindUserResponse> results;

    public List<FindUserResponse> getResults() {
        return results;
    }

    public void setResults(List<FindUserResponse> results) {
        this.results = results;
    }
}
