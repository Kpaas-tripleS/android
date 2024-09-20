package com.example.android.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.example.android.ui.global.RetrofitClient;
import com.example.android.api.UserApi;
import com.example.android.dto.request.SignUpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nicknameEditText;
    private EditText phoneEditText;
    private Button signUpButton;

    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        nicknameEditText = findViewById(R.id.nickname);
        phoneEditText = findViewById(R.id.phone);
        signUpButton = findViewById(R.id.finish);

        userApi = RetrofitClient.getInstance().getApiService();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String nickname = nicknameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || nickname.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(SignUp.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SignUpRequest signUpRequest = new SignUpRequest(name, email, password, nickname, phone);

                userApi.signUp(signUpRequest)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                                    // 회원가입 성공 후 다음 작업을 수행할 수 있습니다.
                                } else {
                                    String errorMessage = "";
                                    try {
                                        errorMessage = response.errorBody().string();
                                    } catch (Exception e) {
                                        errorMessage = "알 수 없는 오류가 발생했습니다.";
                                    }
                                    Toast.makeText(SignUp.this, "회원가입 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(SignUp.this, "서버와의 연결에 실패했습니다: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
