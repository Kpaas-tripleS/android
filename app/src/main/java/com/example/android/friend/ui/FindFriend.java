package com.example.android.friend.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindFriend extends AppCompatActivity implements FindFriendAdapter.OnFriendDeleteListener {
    private SearchView searchView;
    private RecyclerView friendRecyclerView;
    private FindFriendAdapter findFriendAdapter;
    private List<FriendResponse> friendList = new ArrayList<>();
    private List<FriendResponse> filteredList = new ArrayList<>();
    private FriendApi friendApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_friend);

        searchView = findViewById(R.id.searchView);
        friendRecyclerView = findViewById(R.id.friendRequestRecyclerView);

        friendApi = RetrofitClient.getInstance(this).getFriendApi();

        int numberOfColumns = 2;
        friendRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        findFriendAdapter = new FindFriendAdapter(filteredList, this, this); // this로 리스너 전달
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
}
