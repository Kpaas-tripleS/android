package com.example.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.example.android.ui.global.RetrofitClient;
import com.example.android.api.UserApi;
import com.example.android.dto.request.LoginRequest;
import com.example.android.dto.response.LoginResponse;
import com.kakao.sdk.user.UserApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private EditText idEditText;
    private EditText pwEditText;
    private Button loginButton;
    private ImageButton kakaoButton;
    private TextView signUpTextView;

    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        idEditText = findViewById(R.id.id);
        pwEditText = findViewById(R.id.pw);
        loginButton = findViewById(R.id.login);
        kakaoButton = findViewById(R.id.imageButton);
        signUpTextView = findViewById(R.id.signUp);

        userApi = RetrofitClient.getInstance().getApiService();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = idEditText.getText().toString();
                String email = ""; // 이메일이 필요 없는 경우는 비워두기
                String password = pwEditText.getText().toString();

                login(nickname, email, password);
            }
        });

        kakaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApiClient.getInstance().loginWithKakaoTalk(Login.this, (oAuthToken, error) -> {
                    if (error != null) {
                        Toast.makeText(Login.this, "회원이 아닙니다.", Toast.LENGTH_SHORT).show();
                    } else if (oAuthToken != null) {
                        fetchUserInfo();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    return null;
                });
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void login(String nickname, String email, String password) {
        LoginRequest loginRequest = new LoginRequest(nickname, email, password);

        Call<LoginResponse> call = userApi.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "로그인 실패: ID 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, "서버와의 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserInfo() {
        UserApiClient.getInstance().me((user, error) -> {
            if (error != null) {
                Toast.makeText(Login.this, "사용자 정보 요청 실패", Toast.LENGTH_SHORT).show();
            }
            return null;
        });
    }
}
