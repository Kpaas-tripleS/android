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
    private Befriend befriend;
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
        BeFriendRequest currentRequest = requests.get(position);

        holder.nicknameTextView.setText(currentRequest.getNickname());

//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, Befriend.class);
//            intent.putExtra("nickname", currentRequest.getNickname());
//            context.startActivity(intent);
//        });
        holder.itemView.setOnClickListener(v -> {
            // Call the acceptFriendRequests method from the Befriend activity
            befriend.acceptFriendRequests(currentRequest, position);
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
