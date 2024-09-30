package com.example.android.match.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class QuizDto implements Parcelable {

    @SerializedName("quizId")
    private Long quizId;

    @SerializedName("question")
    private String question;

    @SerializedName("choiceOne")
    private String choiceOne;

    @SerializedName("choiceTwo")
    private String choiceTwo;

    @SerializedName("choiceThree")
    private String choiceThree;

    @SerializedName("choiceFour")
    private String choiceFour;

    @SerializedName("answer")
    private String answer;

    @SerializedName("isSolved")
    private Boolean isSolved;

    @SerializedName("incorrectCount")
    private Integer incorrectCount;

    @SerializedName("level")
    private DifficultyLevel level;

    public enum DifficultyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED, RANDOM
    }

    // 기본 생성자
    public QuizDto() {}

    // Parcelable 생성자
    protected QuizDto(Parcel in) {
        if (in.readByte() == 0) {
            quizId = null;
        } else {
            quizId = in.readLong();
        }
        question = in.readString();
        choiceOne = in.readString();
        choiceTwo = in.readString();
        choiceThree = in.readString();
        choiceFour = in.readString();
        answer = in.readString();
        byte tmpIsSolved = in.readByte();
        isSolved = tmpIsSolved == 0 ? null : tmpIsSolved == 1;
        if (in.readByte() == 0) {
            incorrectCount = null;
        } else {
            incorrectCount = in.readInt();
        }
        String levelString = in.readString();
        level = levelString == null ? null : DifficultyLevel.valueOf(levelString);
    }

    public static final Creator<QuizDto> CREATOR = new Creator<QuizDto>() {
        @Override
        public QuizDto createFromParcel(Parcel in) {
            return new QuizDto(in);
        }

        @Override
        public QuizDto[] newArray(int size) {
            return new QuizDto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        if (quizId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(quizId);
        }
        parcel.writeString(question);
        parcel.writeString(choiceOne);
        parcel.writeString(choiceTwo);
        parcel.writeString(choiceThree);
        parcel.writeString(choiceFour);
        parcel.writeString(answer);
        parcel.writeByte((byte) (isSolved == null ? 0 : isSolved ? 1 : 2));
        if (incorrectCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(incorrectCount);
        }
        parcel.writeString(level == null ? null : level.name());
    }

    // Getters and setters
    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoiceOne() {
        return choiceOne;
    }

    public void setChoiceOne(String choiceOne) {
        this.choiceOne = choiceOne;
    }

    public String getChoiceTwo() {
        return choiceTwo;
    }

    public void setChoiceTwo(String choiceTwo) {
        this.choiceTwo = choiceTwo;
    }

    public String getChoiceThree() {
        return choiceThree;
    }

    public void setChoiceThree(String choiceThree) {
        this.choiceThree = choiceThree;
    }

    public String getChoiceFour() {
        return choiceFour;
    }

    public void setChoiceFour(String choiceFour) {
        this.choiceFour = choiceFour;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getIsSolved() {
        return isSolved;
    }

    public void setIsSolved(Boolean isSolved) {
        this.isSolved = isSolved;
    }

    public Integer getIncorrectCount() {
        return incorrectCount;
    }

    public void setIncorrectCount(Integer incorrectCount) {
        this.incorrectCount = incorrectCount;
    }

    public DifficultyLevel getLevel() {
        return level;
    }

    public void setLevel(DifficultyLevel level) {
        this.level = level;
    }
}