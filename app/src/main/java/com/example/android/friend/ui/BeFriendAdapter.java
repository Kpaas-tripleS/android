package com.example.android.friend.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.friend.dto.request.BeFriendRequest;
import com.example.android.friend.dto.response.BeFriendResponse;

import java.util.List;

public class BeFriendAdapter extends RecyclerView.Adapter<BeFriendAdapter.ViewHolder> {
    private List<BeFriendResponse> requests;
    private Befriend befriend;
    private BeFriendRequest beFriendRequest;

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
        Long userId = getUserIdFromPreferences(holder.itemView.getContext());

        holder.acceptButton.setOnClickListener(v -> {
            BeFriendRequest beFriendRequest = new BeFriendRequest(1L, requesterId, true);
            befriend.acceptFriendRequests(beFriendRequest, position);
        });
    }


    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nicknameTextView;
        Button acceptButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nicknameTextView = itemView.findViewById(R.id.nickname);
            acceptButton = itemView.findViewById(R.id.acceptButton);
        }
    }
    private Long getUserIdFromPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getLong("userId", -1);
    }
}
