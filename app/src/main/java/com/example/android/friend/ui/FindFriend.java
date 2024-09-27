package com.example.android.friend.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.friend.api.FriendApi;
import com.example.android.friend.dto.response.FriendResponse;
import com.example.android.friend.dto.response.FriendResponseList;
import com.example.android.global.ResponseTemplate;
import com.example.android.global.RetrofitClient;
import com.example.android.match.API.MatchAPI;
import com.example.android.match.activity.MatchActivity;
import com.example.android.match.dto.request.MatchRequest;
import com.example.android.match.dto.response.MatchStartResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.dto.StompHeader;

public class FindFriend extends AppCompatActivity implements FindFriendAdapter.OnFriendDeleteListener, FindFriendAdapter.OnFriendMatchListener {

    private String token;
    private String url;
    private Long matchId;
    private ua.naiksoftware.stomp.StompClient stompClient;
    private SearchView searchView;
    private RecyclerView friendRecyclerView;
    private FindFriendAdapter findFriendAdapter;
    private List<FriendResponse> friendList = new ArrayList<>();
    private List<FriendResponse> filteredList = new ArrayList<>();
    private FriendApi friendApi;
    private MatchAPI matchAPI;
    private FrameLayout match_fail_toast;
    private RelativeLayout match_friend_loading;
    private ImageButton match_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_friend);

        RetrofitClient retrofit = RetrofitClient.getInstance(this);
        token = retrofit.getAccessToken(this);

        searchView = findViewById(R.id.searchView);
        friendRecyclerView = findViewById(R.id.friendRequestRecyclerView);
        match_fail_toast = findViewById(R.id.match_fail_toast);
        match_friend_loading = findViewById(R.id.match_friend_loading);
        match_delete = findViewById(R.id.match_delete);

        friendApi = retrofit.getFriendApi();
        matchAPI = retrofit.getMatchAPI();

        int numberOfColumns = 2;
        friendRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        findFriendAdapter = new FindFriendAdapter(filteredList, this, this, this); // this로 리스너 전달
        friendRecyclerView.setAdapter(findFriendAdapter);

        // 친구 목록 로드
        loadFriends();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterFriends(newText);
                return true;
            }
        });

        match_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<com.example.android.global.dto.response.ResponseTemplate<Void>> call = matchAPI.deleteMatch(matchId);
                call.enqueue(new Callback<com.example.android.global.dto.response.ResponseTemplate<Void>>() {
                    @Override
                    public void onResponse(Call<com.example.android.global.dto.response.ResponseTemplate<Void>> call, Response<com.example.android.global.dto.response.ResponseTemplate<Void>> response) {
                        if(response.isSuccessful()) {
                            match_friend_loading.setVisibility(View.GONE);
                            stompClient.disconnect();
                        }
                    }
                    @Override
                    public void onFailure(Call<com.example.android.global.dto.response.ResponseTemplate<Void>> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void loadFriends() {
        friendApi.getFriends().enqueue(new Callback<ResponseTemplate<FriendResponseList>>() {
            @Override
            public void onResponse(Call<ResponseTemplate<FriendResponseList>> call, Response<ResponseTemplate<FriendResponseList>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FriendResponseList responseList = response.body().getResults();
                    friendList = responseList != null ? responseList.getRequests() : new ArrayList<>();
                    filteredList.clear();
                    filteredList.addAll(friendList);
                    findFriendAdapter.notifyDataSetChanged();
                } else {
                    Log.e("APIError", "Failed to load friends. Code: " + response.code() + ", Message: " + response.message());
                    Toast.makeText(FindFriend.this, "친구 목록 로드 실패.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTemplate<FriendResponseList>> call, Throwable t) {
                Log.e("APIError", "Error in loading friends: " + t.getMessage());
                Toast.makeText(FindFriend.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 친구 삭제 메서드
    @Override
    public void onDeleteFriend(Long friendId, int position) {
        friendApi.deleteFriend(friendId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    friendList.remove(position);
                    filteredList.remove(position);
                    findFriendAdapter.notifyItemRemoved(position);
                    findFriendAdapter.notifyItemRangeChanged(position, friendList.size());
                    Toast.makeText(FindFriend.this, "친구가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FindFriend.this, "친구 삭제 실패. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FindFriend.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //친구 대결 신청 메소드
    @Override
    public void onRequestMatch(Long friendId) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDateTime = sdf.format(calendar.getTime());
        MatchRequest matchRequest = new MatchRequest(formattedDateTime);
        matchAPI.friendMatch(friendId, matchRequest).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if(response.isSuccessful()) {
                    matchId = response.body();
                    url = "/topic/matches/" + matchId;
                    match_friend_loading.setVisibility(View.VISIBLE);
                    startStomp();
                }
                else {
                    Toast.makeText(FindFriend.this, "대결 신청 실패. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(FindFriend.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 필터링 메서드
    private void filterFriends(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(friendList);
        } else {
            for (FriendResponse friend : friendList) {
                if (friend.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(friend);
                }
            }
        }
        findFriendAdapter.notifyDataSetChanged();
    }

    private void startStomp() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/ws");
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("Authorization", "Bearer " + token));
        stompClient.connect(headers);
        stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            System.out.println("WebSocket 연결 성공");
                            subscribeToTopic();
                            break;
                        case ERROR:
                            System.err.println("WebSocket 오류: " + lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            System.out.println("WebSocket 연결 종료");
                            break;
                    }
                });
    }

    private void subscribeToTopic() {
        stompClient.topic(url).subscribe(topicMessage -> {
            handleStomp(topicMessage.getPayload());
        }, throwable -> {
            // onError handler
            Log.e("WebSocket", "Error sending message: " + throwable.getMessage());
        });
    }

    private void handleStomp(String message) {
        if (message.equals("MATCH_FAIL")) {
            handleMatchFail();
        } else {
            Observable.fromCallable(() -> {
                        Gson gson = new GsonBuilder().create();
                        return gson.fromJson(message, MatchStartResponse.class);
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        handleMatchComplete(matchId, result);
                    }, throwable -> {
                        Log.e("JSONParseError", "Error parsing message: " + throwable.getMessage());
                    });
        }
    }

    private void handleMatchComplete(Long matchId, MatchStartResponse result) {
        runOnUiThread(() -> match_friend_loading.setVisibility(View.GONE));
        Intent intent = new Intent(FindFriend.this, MatchActivity.class);
        intent.putExtra("MATCH_ID", matchId);
        intent.putExtra("MATCH_START_RESPONSE", result);
        startActivity(intent);
        finish();
        stompClient.disconnectCompletable()
                .subscribe(() -> Log.d("WebSocket", "Disconnected successfully"),
                        throwable -> Log.e("WebSocket", "Disconnection failed", throwable));
    }

    private void handleMatchFail() {
        runOnUiThread(() -> {
            match_friend_loading.setVisibility(View.GONE);
            match_fail_toast.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> match_fail_toast.setVisibility(View.GONE), 2000);
        });
        stompClient.disconnectCompletable()
                .subscribe(() -> Log.d("WebSocket", "Disconnected successfully"),
                        throwable -> Log.e("WebSocket", "Disconnection failed", throwable));
    }


}
