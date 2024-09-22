package com.example.android.friend.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.friend.dto.request.BeFriendRequest;

import java.util.ArrayList;
import java.util.List;

public class ChildAdapter {
    private List<BeFriendRequest> befriendRequests;


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_befriend_request, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        BeFriendRequest request = befriendRequests.get(position);
//        BeFriend beFriend = new BeFriend(request.getRequesterId().toString(), "이름", R.drawable.ic_launcher_background);

//        List<BeFriend> beFriends = new ArrayList<>();
//        beFriends.add(beFriend);

//        ChildAdapter childAdapter = new ChildAdapter(beFriends);
//        holder.friendRecyclerView.setAdapter(childAdapter);
    }

    public int getItemCount() {
        return befriendRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView friendRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            friendRecyclerView = itemView.findViewById(R.id.friendRequestRecyclerView);
        }
    }
}

