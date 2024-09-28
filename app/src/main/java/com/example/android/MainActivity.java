package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;

import com.example.android.friend.ui.Befriend;
import com.example.android.match.activity.MatchMainActivity;
import com.example.android.quiz.activity.QuizActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 메인 뷰에 대한 WindowInsetsListener 설정
        setupWindowInsets();
    }

    private void setupWindowInsets() {
        View mainView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // 시스템 바의 여백을 뷰의 패딩으로 설정
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button goToMatch = findViewById(R.id.go_to_match); //임시 버튼
        goToMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Befriend.class);
                startActivity(intent);
            }
        });

        Button goToQuiz = findViewById(R.id.go_to_quiz); // XML에 이 버튼을 추가해야 합니다
        goToQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

    }
}
