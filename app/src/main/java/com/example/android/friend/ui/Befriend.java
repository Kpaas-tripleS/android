package com.example.android.friend.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.friend.api.BeFriendApi;
import com.example.android.friend.dto.request.BeFriendRequest;
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
    private ChildAdapter childAdapter;
    private List<BeFriendRequest> befriendRequests = new ArrayList<>(); // 친구 요청을 저장할 리스트
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.be_friend);

        beFriendApi = RetrofitClient.getInstance(this).getBefriendApi();

        friendRequestRecyclerView = findViewById(R.id.friendRequestRecyclerView);
        friendRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        childAdapter = new ChildAdapter(befriendRequests, this);
        friendRequestRecyclerView.setAdapter(childAdapter);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        loadFriendRequests();
    }

    private void loadFriendRequests() {
        beFriendApi.getFriendRequestList().enqueue(new Callback<ResponseTemplate<BeFriendResponseList>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ResponseTemplate<BeFriendResponseList>> call, Response<ResponseTemplate<BeFriendResponseList>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<BeFriendRequest> requests = response.body().getResults().getRequests();

                    Log.d("APIResponse", "Received requests: " + requests);

                    if (requests != null && !requests.isEmpty()) {

                        List<String> nicknames = new ArrayList<>();
                        for (BeFriendRequest request : requests) {
                            nicknames.add(request.getNickname());
                        }

                        befriendRequests.clear();
                        for (String nickname : nicknames) {
                            befriendRequests.add(new BeFriendRequest(null, nickname)); // 새로운 BeFriendRequest 객체 생성
                        }
                        childAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("APIError", "No requests found");
                        Toast.makeText(Befriend.this, "친구 요청이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("APIError", "Response Code: " + response.code() + ", Message: " + response.message());
                    Toast.makeText(Befriend.this, "친구 요청 로드 실패.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTemplate<BeFriendResponseList>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Befriend.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addRequestToRecyclerView(BeFriendRequest request) {
        befriendRequests.add(request);
        childAdapter.notifyItemInserted(befriendRequests.size() - 1);
    }
}
