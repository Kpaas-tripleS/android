package com.example.android.friend.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.friend.api.BeFriendApi;
import com.example.android.friend.dto.request.BeFriendRequest;
import com.example.android.friend.dto.response.BeFriendResponseList;
import com.example.android.global.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Befriend extends AppCompatActivity {
    private RecyclerView friendRequestRecyclerView;
    private BeFriendApi beFriendApi;
    private ChildAdapter childAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.be_friend);

        beFriendApi = RetrofitClient.getInstance(this).getBefriendApi();
        sendFriendRequest(2L);

        friendRequestRecyclerView = findViewById(R.id.friendRequestRecyclerView);
        friendRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        loadFriendRequests();
    }

    public void sendFriendRequest(Long receiverId) {
        BeFriendRequest request = new BeFriendRequest(receiverId);
        Call<Void> call = beFriendApi.sendFriendRequest(request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 친구 요청 성공
                    System.out.println("Friend request sent successfully.");
                } else {
                    // 친구 요청 실패
                    System.out.println("Failed to send friend request: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 네트워크 오류
                System.out.println("Network error: " + t.getMessage());
            }
        });
    }

    private void loadFriendRequests() {
        beFriendApi.getFriendRequestList().enqueue(new Callback<BeFriendResponseList>() {
            @Override
            public void onResponse(Call<BeFriendResponseList> call, Response<BeFriendResponseList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BeFriendRequest> requests = response.body().getRequests();

//                    childAdapter = new ChildAdapter(requests);
//                    friendRequestRecyclerView.setAdapter(childAdapter);
//

                    for (BeFriendRequest request : requests) {
                        addRequestToRecyclerView(request);
                    }
                } else {
                    Toast.makeText(Befriend.this, "친구 요청을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BeFriendResponseList> call, Throwable t) {
                Toast.makeText(Befriend.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRequestToRecyclerView(BeFriendRequest request) {
//        befriendRequest.add(request);
//        childAdapter.notifyItemInserted(befriendRequests.size() - 1);
    }
}