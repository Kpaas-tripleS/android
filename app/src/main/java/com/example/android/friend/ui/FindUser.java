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
import com.example.android.global.RetrofitClient;
import com.example.android.user.api.UserApi;
import com.example.android.user.dto.response.FindUserResponse;


import java.util.ArrayList;
import java.util.List;


public class FindUser extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView userRecyclerView;
    private FindUserAdapter findUserAdapter;
    private List<FindUserResponse> userList = new ArrayList<>();
    private List<FindUserResponse> filteredList = new ArrayList<>();
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_user);

        searchView = findViewById(R.id.searchView);
        userRecyclerView = findViewById(R.id.userRecyclerView);

        userApi = RetrofitClient.getInstance(this).getFindUser();

        int numberOfColumns = 2;
        userRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        findUserAdapter = new FindUserAdapter(filteredList, this);
        userRecyclerView.setAdapter(findUserAdapter);

//        // 전체 사용자 목록 로드
//        loadUsers();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });
    }

//    // 사용자 목록 로드
//    private void loadUsers() {
//        progressBar.setVisibility(View.VISIBLE);
//        userApi.getUsers().enqueue(new Callback<ResponseTemplate<UserResponseList>>() {
//            @Override
//            public void onResponse(Call<ResponseTemplate<UserResponseList>> call, Response<ResponseTemplate<UserResponseList>> response) {
//                progressBar.setVisibility(View.GONE);
//                if (response.isSuccessful() && response.body() != null) {
//                    UserResponseList responseList = response.body().getResults();
//                    userList = responseList != null ? responseList.getUsers() : new ArrayList<>();
//                    filteredList.clear();
//                    filteredList.addAll(userList);
//                    findUserAdapter.notifyDataSetChanged();
//                } else {
//                    Log.e("APIError", "Failed to load users. Code: " + response.code() + ", Message: " + response.message());
//                    Toast.makeText(FindUser.this, "사용자 목록 로드 실패.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseTemplate<UserResponseList>> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
//                Log.e("APIError", "Error in loading users: " + t.getMessage());
//                Toast.makeText(FindUser.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    // 검색어에 따른 사용자 필터링
    private void filterUsers(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(userList); // 검색어가 없으면 전체 사용자 추가
        } else {
            for (FindUserResponse user : userList) {
                if (user.getNickname().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(user); // 검색어가 포함된 사용자 추가
                }
            }
        }
        findUserAdapter.notifyDataSetChanged(); // 어댑터에 데이터 변경 알리기
    }
}
