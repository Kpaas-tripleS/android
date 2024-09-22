package com.example.android.main.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;

import com.example.android.R;
import com.example.android.friend.api.BeFriendApi;
import com.example.android.friend.dto.request.BeFriendRequest;
import com.example.android.global.RetrofitClient;
import com.example.android.user.api.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button loginButton;
    private UserApi userApi;
    private BeFriendApi beFriendApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.login);

        beFriendApi = RetrofitClient.getInstance(this).getBefriendApi();

        setupWindowInsets();
        userApi = RetrofitClient.getInstance(this).getUserApi();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFriendRequest(2L);
            }
        });
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

    private void setupWindowInsets() {
        View mainView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
