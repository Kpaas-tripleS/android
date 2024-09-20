package com.example.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;

import java.util.ArrayList;
import java.util.List;

public class Befriend extends AppCompatActivity {
    private RecyclerView friendRequestRecyclerView;
    private List<FriendRequest> friendRequests; // friendRequests 리스트 정의

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.be_friend);

        friendRequestRecyclerView = findViewById(R.id.friendRequestRecyclerView);
        friendRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendRequests = new ArrayList<>();

        FriendRequestAdapter adapter = new FriendRequestAdapter(friendRequests);
        friendRequestRecyclerView.setAdapter(adapter);
    }

    public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
        private List<FriendRequest> friendRequests;

        public FriendRequestAdapter(List<FriendRequest> friendRequests) {
            this.friendRequests = friendRequests;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_befriend_request, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ChildAdapter childAdapter = new ChildAdapter(friendRequests.get(position).getFriends());
            holder.friendRecyclerView.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), 2));
            holder.friendRecyclerView.setAdapter(childAdapter);
        }

        @Override
        public int getItemCount() {
            return friendRequests.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            RecyclerView friendRecyclerView;

            public ViewHolder(View itemView) {
                super(itemView);
                friendRecyclerView = itemView.findViewById(R.id.friendRequestRecyclerView);
            }
        }
    }

    public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {
        private List<Friend> friends;

        public ChildAdapter(List<Friend> friends) {
            this.friends = friends;
        }

        @Override
        public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
            return new ChildViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChildViewHolder holder, int position) {
            Friend friend = friends.get(position);
            holder.nameTextView.setText(friend.getName());
            holder.profileImageView.setImageResource(friend.getProfileImageResId());
            holder.acceptButton.setOnClickListener(v -> {
                // 친구 수락 로직 추가
            });
        }

        @Override
        public int getItemCount() {
            return friends.size();
        }

        public class ChildViewHolder extends RecyclerView.ViewHolder {
            ImageView profileImageView;
            TextView nameTextView;
            Button acceptButton;

            public ChildViewHolder(View itemView) {
                super(itemView);
                profileImageView = itemView.findViewById(R.id.friendProfileImage);
                nameTextView = itemView.findViewById(R.id.friendNameText);
                acceptButton = itemView.findViewById(R.id.acceptButton);
            }
        }
    }

    public class FriendRequest {
        private List<Friend> friends;

        public FriendRequest(List<Friend> friends) {
            this.friends = friends;
        }

        public List<Friend> getFriends() {
            return friends;
        }
    }

    public class Friend {
        private String name;
        private int profileImageResId; // 프로필 이미지 리소스 ID 추가

        public Friend(String name, int profileImageResId) {
            this.name = name;
            this.profileImageResId = profileImageResId;
        }

        public String getName() {
            return name;
        }

        public int getProfileImageResId() {
            return profileImageResId;
        }
    }
}
