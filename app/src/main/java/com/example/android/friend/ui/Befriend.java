package com.example.android.friend.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.friend.api.BeFriendApi;
import com.example.android.friend.dto.request.BeFriendRequest;
import com.example.android.friend.dto.response.BeFriendResponse;
import com.example.android.friend.dto.response.BeFriendResponseList;
import com.example.android.global.ResponseTemplate;
import com.example.android.global.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Befriend extends AppCompatActivity {

    private RecyclerView friendRequestRecyclerView;
    private BeFriendApi beFriendApi;
    private BeFriendAdapter beFriendAdapter;
    private List<BeFriendResponse> beFriendResponses = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.be_friend);

        beFriendApi = RetrofitClient.getInstance(this).getBefriendApi();

        friendRequestRecyclerView = findViewById(R.id.friendRequestRecyclerView);
        int numberOfColumns = 2;
        friendRequestRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        beFriendAdapter = new BeFriendAdapter(beFriendResponses, this);
        friendRequestRecyclerView.setAdapter(beFriendAdapter);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Button findFriendListButton = findViewById(R.id.findFriendList);
        findFriendListButton.setOnClickListener(v -> {
            Intent intent = new Intent(Befriend.this, FindFriend.class);
            startActivity(intent);
        });

        Button findFriendButton = findViewById(R.id.findFriend);
        findFriendButton.setOnClickListener(v -> {
            Intent intent = new Intent(Befriend.this, FindUser.class);
            startActivity(intent);
        });

        loadFriendRequests(3L);
    }

    public void acceptFriendRequests(Long requesterId, int position) {
        BeFriendRequest beFriendRequest = new BeFriendRequest(3L, requesterId, true);
        Log.d("AcceptRequest", "Sending request to accept friend request: " + beFriendRequest.toString());

        beFriendApi.handleFriendRequest(beFriendRequest).enqueue(new Callback<ResponseTemplate<BeFriendRequest>>() {
            @Override
            public void onResponse(Call<ResponseTemplate<BeFriendRequest>> call, Response<ResponseTemplate<BeFriendRequest>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Befriend.this, "친구 수락이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                    beFriendResponses.remove(position);
                    beFriendAdapter.notifyItemRemoved(position);
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseTemplate<BeFriendRequest>> call, Throwable t) {
                handleApiFailure(t);
            }
        });
    }

    public void rejectFriendRequests(Long requesterId, int position) {
        beFriendApi.handleFriendRequest(new BeFriendRequest(3L, requesterId, false)).enqueue(new Callback<ResponseTemplate<BeFriendRequest>>() {
            @Override
            public void onResponse(Call<ResponseTemplate<BeFriendRequest>> call, Response<ResponseTemplate<BeFriendRequest>> response) {
                Log.d("RejectRequest", "Response code: " + response.code() + ", Message: " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Befriend.this, "친구 요청이 거절되었습니다.", Toast.LENGTH_SHORT).show();
                    beFriendResponses.remove(position);
                    beFriendAdapter.notifyItemRemoved(position);
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseTemplate<BeFriendRequest>> call, Throwable t) {
                handleApiFailure(t);
            }
        });
    }


    private void loadFriendRequests(Long userId) {
        beFriendApi.getFriendRequestList(userId).enqueue(new Callback<ResponseTemplate<BeFriendResponseList>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ResponseTemplate<BeFriendResponseList>> call, Response<ResponseTemplate<BeFriendResponseList>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    BeFriendResponseList responseList = response.body().getResults();
                    List<BeFriendResponse> requests = responseList != null ? responseList.getRequests() : new ArrayList<>();

                    if (requests != null && !requests.isEmpty()) {
                        Log.d("APIResponse", "Received friend requests: " + requests.size());
                        beFriendResponses.clear();
                        beFriendResponses.addAll(requests);
                        beFriendAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("APIResponse", "No friend requests available");
                        Toast.makeText(Befriend.this, "친구 요청이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseTemplate<BeFriendResponseList>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                handleApiFailure(t);
            }
        });
    }

    private void handleApiError(Response<?> response) {
        Log.e("APIError", "API error. Code: " + response.code() + ", Message: " + response.message());
        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                Log.e("request_APIError", "Error body: " + errorBody);
                Toast.makeText(Befriend.this, "오류 발생: " + errorBody, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("request_APIError", "Error parsing error body", e);
                Toast.makeText(Befriend.this, "서버 오류.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleApiFailure(Throwable t) {
        Log.e("request_APIError", "API failure: " + t.getMessage());
        Toast.makeText(Befriend.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
