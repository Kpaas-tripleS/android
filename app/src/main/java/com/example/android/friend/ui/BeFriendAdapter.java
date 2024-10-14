package com.example.android.friend.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.friend.dto.response.BeFriendResponse;

import java.util.List;

public class BeFriendAdapter extends RecyclerView.Adapter<BeFriendAdapter.ViewHolder> {
    private List<BeFriendResponse> requests;
    private Befriend befriend;

    public BeFriendAdapter(List<BeFriendResponse> requests, Befriend befriend) {
        this.requests = requests;
        this.befriend = befriend;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_befriend_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BeFriendResponse beFriendResponse = requests.get(position);
        holder.nicknameTextView.setText(beFriendResponse.getNickname());

        Long requesterId = beFriendResponse.getRequesterId();

        holder.acceptButton.setOnClickListener(v -> {
            befriend.acceptFriendRequests(requesterId, position);
        });

        holder.declineButton.setOnClickListener(v -> {
            befriend.rejectFriendRequests(requesterId, position);
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nicknameTextView;
        Button acceptButton;
        Button declineButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nicknameTextView = itemView.findViewById(R.id.nickname);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }
}
