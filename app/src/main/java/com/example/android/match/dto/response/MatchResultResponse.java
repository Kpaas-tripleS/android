package com.example.android.match.dto.response;

import com.google.gson.annotations.SerializedName;

public class MatchResultResponse {

   @SerializedName("matchId")
   private Long matchId;

   @SerializedName("player")
   private UserResponse player;

   @SerializedName("opponent")
   private UserResponse opponent;

   @SerializedName("playerScore")
   private Long playerScore;

   @SerializedName("opponentScore")
   private Long opponentScore;

   public Long getMatchId() {
      return matchId;
   }

   public void setMatchId(Long matchId) {
      this.matchId = matchId;
   }

   public UserResponse getPlayer() {
      return player;
   }

   public void setPlayer(UserResponse player) {
      this.player = player;
   }

   public UserResponse getOpponent() {
      return opponent;
   }

   public void setOpponent(UserResponse opponent) {
      this.opponent = opponent;
   }

   public Long getPlayerScore() {
      return playerScore;
   }

   public void setPlayerScore(Long playerScore) {
      this.playerScore = playerScore;
   }

   public Long getOpponentScore() {
      return opponentScore;
   }

   public void setOpponentScore(Long opponentScore) {
      this.opponentScore = opponentScore;
   }
}
