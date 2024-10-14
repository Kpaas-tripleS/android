package com.example.android.friend.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.friend.api.BeFriendApi;
import com.example.android.friend.dto.request.BeFriendRequest;
import com.example.android.global.ResponseTemplate;
import com.example.android.global.RetrofitClient;
import com.example.android.user.api.UserApi;
import com.example.android.user.dto.response.FindUserResponse;
import com.example.android.user.dto.response.FindUserResponseList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindUser extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView userRecyclerView;
    private FindUserAdapter findUserAdapter;
    private List<FindUserResponse> userList = new ArrayList<>();
    private List<FindUserResponse> filteredList = new ArrayList<>();
    private UserApi userApi;
    private BeFriendApi beFriendApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_user);

        searchView = findViewById(R.id.searchView);
        userRecyclerView = findViewById(R.id.userRecyclerView);

        RetrofitClient retrofit = RetrofitClient.getInstance(this);
        userApi = retrofit.getFindUser();
        beFriendApi = retrofit.getBefriendApi(); // BeFriendApi 인스턴스 가져오기

        int numberOfColumns = 2;
        userRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        // sendFriendRequest 메서드를 참조하여 어댑터에 전달
        findUserAdapter = new FindUserAdapter(filteredList, this, this::sendFriendRequest);
        userRecyclerView.setAdapter(findUserAdapter);

        // Load all users initially
        loadAllUsers();

        // Set up the search view listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });
    }

    private void loadAllUsers() {
        userApi.findUser().enqueue(new Callback<ResponseTemplate<FindUserResponseList>>() {
            @Override
            public void onResponse(Call<ResponseTemplate<FindUserResponseList>> call, Response<ResponseTemplate<FindUserResponseList>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FindUserResponseList responseList = response.body().getResults();
                    userList = responseList != null ? responseList.getResults() : new ArrayList<>();
                    filteredList.clear();
                    filteredList.addAll(userList);
                    findUserAdapter.notifyDataSetChanged();
                } else {
                    Log.e("APIError", "Failed to load users. Code: " + response.code() + ", Message: " + response.message());
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseTemplate<FindUserResponseList>> call, Throwable t) {
                Log.e("APIError", "Error in loading users: " + t.getMessage());
            }
        });
    }

    private void filterUsers(String query) {
        List<FindUserResponse> filtered = new ArrayList<>();
        for (FindUserResponse user : userList) {
            if (user.getNickname().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(user);
            }
        }
        filteredList.clear();
        filteredList.addAll(filtered);
        findUserAdapter.notifyDataSetChanged();
    }

    private void sendFriendRequest(Long receiverId) {
        BeFriendRequest beFriendRequest = new BeFriendRequest(receiverId, 1L, false);
        beFriendApi.sendFriendRequest(beFriendRequest).enqueue(new Callback<ResponseTemplate<Void>>() {
            @Override
            public void onResponse(Call<ResponseTemplate<Void>> call, Response<ResponseTemplate<Void>> response) {
                if (response.isSuccessful()) {
                    // 친구 신청 성공 시, 해당 사용자를 리스트에서 제거
                    removeUserById(receiverId);
                    Toast.makeText(FindUser.this, "친구 신청이 전송되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 오류 발생 시 errorBody 처리
                    String errorMessage = "친구 신청 실패: " + response.message();
                    handleErrorResponse(response);
                    Toast.makeText(FindUser.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTemplate<Void>> call, Throwable t) {
                Toast.makeText(FindUser.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FriendRequestError", "Error in sending friend request: " + t.getMessage());
            }
        });
    }

    private void removeUserById(Long userId) {
        for (FindUserResponse user : filteredList) {
            if (user.getUserId().equals(userId)) {
                filteredList.remove(user);
                break; // 사용자 제거 후 반복 종료
            }
        }
        findUserAdapter.notifyDataSetChanged(); // 어댑터에 변경 사항 알리기
    }

    private void handleErrorResponse(Response<?> response) {
        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                Log.e("APIError", "Error body: " + errorBody);
            } catch (Exception e) {
                Log.e("APIError", "Error parsing error body", e);
            }
        }
    }
}
