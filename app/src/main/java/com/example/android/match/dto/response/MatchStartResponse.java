package com.example.android.match.dto.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.example.android.match.dto.QuizDto;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MatchStartResponse implements Parcelable {

    @SerializedName("matchId")
    private Long matchId;

    @SerializedName("player")
    private UserResponse player;

    @SerializedName("opponent")
    private UserResponse opponent;

    @SerializedName("quizzes")
    private List<QuizDto> quizzes;

    // 기본 생성자
    public MatchStartResponse() {}

    protected MatchStartResponse(Parcel in) {
        matchId = in.readLong();
        player = in.readParcelable(UserResponse.class.getClassLoader());
        opponent = in.readParcelable(UserResponse.class.getClassLoader());
        quizzes = in.createTypedArrayList(QuizDto.CREATOR);
    }

    public static final Creator<MatchStartResponse> CREATOR = new Creator<MatchStartResponse>() {
        @Override
        public MatchStartResponse createFromParcel(Parcel in) {
            return new MatchStartResponse(in);
        }

        @Override
        public MatchStartResponse[] newArray(int size) {
            return new MatchStartResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(matchId);
        parcel.writeParcelable(player, flags);
        parcel.writeParcelable(opponent, flags);
        parcel.writeTypedList(quizzes);
    }

    // Getters and setters...
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

    public List<QuizDto> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<QuizDto> quizzes) {
        this.quizzes = quizzes;
    }
}


/*package com.example.android.match.dto.response;

import com.example.android.match.dto.QuizDto;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MatchStartResponse {

    @SerializedName("matchId")
    private Long matchId;

    @SerializedName("leader")
    private UserResponse leader;

    @SerializedName("follower")
    private UserResponse follower;

    @SerializedName("quizzes")
    private List<QuizDto> quizzes;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public UserResponse getLeader() {
        return leader;
    }

    public void setLeader(UserResponse leader) {
        this.leader = leader;
    }

    public UserResponse getFollower() {
        return follower;
    }

    public void setFollower(UserResponse follower) {
        this.follower = follower;
    }

    public List<QuizDto> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<QuizDto> quizzes) {
        this.quizzes = quizzes;
    }

}*/

