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
import com.example.android.friend.dto.response.FriendResponse;

import java.util.List;

public class FindFriendAdapter extends RecyclerView.Adapter<FindFriendAdapter.ViewHolder> {
    private List<FriendResponse> friends;
    private Context context;
    private OnFriendDeleteListener onFriendDeleteListener;

    public interface OnFriendDeleteListener {
        void onDeleteFriend(Long friendId, int position);
    }

    public FindFriendAdapter(List<FriendResponse> friends, Context context, OnFriendDeleteListener onFriendDeleteListener) {
        this.friends = friends;
        this.context = context;
        this.onFriendDeleteListener = onFriendDeleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_find_friend_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendResponse friend = friends.get(position);
        holder.nicknameTextView.setText(friend.getName());

        holder.deleteButton.setOnClickListener(v -> {
            onFriendDeleteListener.onDeleteFriend(friend.getId(), position);
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nicknameTextView;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nicknameTextView = itemView.findViewById(R.id.nickname);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
