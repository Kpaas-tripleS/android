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

        userApi = RetrofitClient.getInstance(this).getUserApi();
    }
}
