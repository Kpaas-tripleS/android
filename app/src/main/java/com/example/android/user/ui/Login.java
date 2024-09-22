package com.example.android.user.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.example.android.global.ResponseTemplate;
import com.example.android.main.ui.MainActivity;
import com.example.android.global.KakaoInit;
import com.example.android.global.RetrofitClient;
import com.example.android.user.api.UserApi;
import com.example.android.user.dto.request.LoginRequest;
import com.example.android.user.dto.response.LoginResponse;
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

    private static final int KAKAO_LOGIN_REQUEST_CODE = 1004; // 요청 코드 정의

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KakaoInit.kakaoSdkInit(this); // 초기화 호출
        setContentView(R.layout.login);

        idEditText = findViewById(R.id.id);
        pwEditText = findViewById(R.id.pw);
        loginButton = findViewById(R.id.login);
        kakaoButton = findViewById(R.id.imageButton);
        signUpTextView = findViewById(R.id.signUp);

        userApi = RetrofitClient.getInstance(this).getUserApi();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = idEditText.getText().toString();
                String email = idEditText.getText().toString();
                String password = pwEditText.getText().toString();

                login(nickname, email, password);
            }
        });

        kakaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithKakao();
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

        Call<ResponseTemplate<LoginResponse>> call = userApi.login(loginRequest);
        call.enqueue(new Callback<ResponseTemplate<LoginResponse>>() {
            @Override
            public void onResponse(Call<ResponseTemplate<LoginResponse>> call, Response<ResponseTemplate<LoginResponse>> response) {
                if (response.isSuccessful()) {
                    ResponseTemplate<LoginResponse> loginResponse = response.body();

                    if (loginResponse != null) {
                        LoginResponse results = loginResponse.getResults();

                        SharedPreferences sharedPreferences = getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("accessToken", results.getAccessToken());
                        editor.putString("refreshToken", results.getRefreshToken());
                        editor.apply();

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "로그인 응답이 비어있습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "로그인 실패: ID 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseTemplate<LoginResponse>> call, Throwable t) {
                Toast.makeText(Login.this, "서버와의 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loginWithKakao() {
        UserApiClient.getInstance().loginWithKakaoTalk(this, (token, error) -> {
            if (error != null) {
                Log.e("KakaoLogin", "로그인 실패", error);
                Toast.makeText(Login.this, "카카오 로그인 실패", Toast.LENGTH_SHORT).show();
            } else if (token != null) {
                Log.d("KakaoLogin", "로그인 성공: " + token.getAccessToken());
                // 여기서 accessToken을 사용하여 서버에 로그인 요청을 보낼 수 있습니다.
                handleKakaoAccessToken(token.getAccessToken());
            }
            return null;
        });
    }

    private void handleKakaoAccessToken(String accessToken) {
        Call<ResponseTemplate<LoginResponse>> call = userApi.kakaoLogin(accessToken);
        call.enqueue(new Callback<ResponseTemplate<LoginResponse>>() {
            @Override
            public void onResponse(Call<ResponseTemplate<LoginResponse>> call, Response<ResponseTemplate<LoginResponse>> response) {
                if (response.isSuccessful()) {
                    ResponseTemplate<LoginResponse> loginResponse = response.body();
                    if (loginResponse != null) {
                        LoginResponse results = loginResponse.getResults();
                        // 토큰 저장 및 다음 액티비티로 이동
                        SharedPreferences sharedPreferences = getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("accessToken", results.getAccessToken());
                        editor.putString("refreshToken", results.getRefreshToken());
                        editor.apply();

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "로그인 응답이 비어있습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "로그인 실패: 잘못된 토큰입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTemplate<LoginResponse>> call, Throwable t) {
                Toast.makeText(Login.this, "서버와의 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == KAKAO_LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        String code = uri.getQueryParameter("code");
                        Log.d("grant code", "default: " + code);
                        if (code != null) {
                        }
                    } else {
                        Log.e("KakaoLogin", "URI is null");
                    }
                } else {
                    Log.e("KakaoLogin", "Intent data is null");
                }
            } else {
                Log.e("KakaoLogin", "Login failed or canceled");
            }
        }
    }

}
