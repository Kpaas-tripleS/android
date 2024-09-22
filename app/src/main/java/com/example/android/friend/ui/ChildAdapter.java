package com.example.android.friend.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.friend.dto.request.BeFriendRequest;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {
    private List<BeFriendRequest> requests;
    private Context context;

    public ChildAdapter(List<BeFriendRequest> requests, Context context) {
        this.requests = requests;
        this.context = context;
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
        // Get the current BeFriendRequest
        BeFriendRequest currentRequest = requests.get(position);

        // Set the nickname in the TextView
        holder.nicknameTextView.setText(currentRequest.getNickname());

        // Handle the click event
        holder.itemView.setOnClickListener(v -> {
            // Intent to navigate to Befriend activity (replace Befriend.class with the correct class)
            Intent intent = new Intent(context, Befriend.class);
            intent.putExtra("nickname", currentRequest.getNickname());
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return requests.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nicknameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nicknameTextView = itemView.findViewById(R.id.nickname);
        }
    }
}
