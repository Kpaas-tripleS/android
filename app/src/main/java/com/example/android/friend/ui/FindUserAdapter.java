package com.example.android.friend.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.user.dto.response.FindUserResponse;

import java.util.List;

public class FindUserAdapter extends RecyclerView.Adapter<FindUserAdapter.ViewHolder> {
    private List<FindUserResponse> users;
    private Context context;
    private OnFriendRequestClickListener friendRequestClickListener;

    public interface OnFriendRequestClickListener {
        void onFriendRequestClick(Long receiverId);
    }

    public FindUserAdapter(List<FindUserResponse> users, Context context, OnFriendRequestClickListener listener) {
        this.users = users;
        this.context = context;
        this.friendRequestClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_find_user_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FindUserResponse user = users.get(position);
        holder.nicknameTextView.setText(user.getNickname());

        // 버튼 클릭 리스너 설정
        holder.acceptButton.setOnClickListener(v -> {
            if (friendRequestClickListener != null) {
                friendRequestClickListener.onFriendRequestClick(user.getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nicknameTextView;
        Button acceptButton; // 버튼 추가

        public ViewHolder(View itemView) {
            super(itemView);
            nicknameTextView = itemView.findViewById(R.id.nickname);
            acceptButton = itemView.findViewById(R.id.acceptButton); // 버튼 초기화
        }
    }
}
