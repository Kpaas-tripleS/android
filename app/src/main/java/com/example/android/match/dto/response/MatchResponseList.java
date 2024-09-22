package com.example.android.match.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MatchResponseList {
   @SerializedName("matchResponseList")
   private List<MatchResponse> matchResponseList;
   public List<MatchResponse> getMatchResponseList() {
      return matchResponseList;
   }
}
