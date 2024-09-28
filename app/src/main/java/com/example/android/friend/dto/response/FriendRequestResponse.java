package com.example.android.friend.dto.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FriendRequestResponse {

    @SerializedName("friendRequests")
    private List<FriendRequest> friendRequests;

    public FriendRequestResponse(List<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public List<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    public static class FriendRequest {

        @SerializedName("friendId")
        private Long friendId;

        @SerializedName("nickname")
        private String nickname;

        @SerializedName("profileImageUrl")
        private String profileImageUrl;

        public FriendRequest(Long friendId, String nickname, String profileImageUrl) {
            this.friendId = friendId;
            this.nickname = nickname;
            this.profileImageUrl = profileImageUrl;
        }

        public Long getFriendId() {
            return friendId;
        }

        public String getNickname() {
            return nickname;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }
    }
}
