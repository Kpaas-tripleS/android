package com.example.android.match.dto.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class UserResponse implements Parcelable {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("profile_image")
    private String profile_image;

    @SerializedName("win_count")
    private Long win_count;

    @SerializedName("grade")
    private Grade grade;

    // 기본 생성자
    public UserResponse() {}

    // Parcelable 생성자
    protected UserResponse(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        nickname = in.readString();
        profile_image = in.readString();
        if (in.readByte() == 0) {
            win_count = null;
        } else {
            win_count = in.readLong();
        }
        grade = Grade.valueOf(in.readString());
    }

    public static final Creator<UserResponse> CREATOR = new Creator<UserResponse>() {
        @Override
        public UserResponse createFromParcel(Parcel in) {
            return new UserResponse(in);
        }

        @Override
        public UserResponse[] newArray(int size) {
            return new UserResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(name);
        parcel.writeString(nickname);
        parcel.writeString(profile_image);
        if (win_count == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(win_count);
        }
        parcel.writeString(grade.name());
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public Long getWin_count() {
        return win_count;
    }

    public Grade getGrade() {
        return grade;
    }
}