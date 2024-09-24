package com.example.android.quiz.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android.R;

public class QuizResultActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        prefs = getSharedPreferences("QuizPrefs", MODE_PRIVATE);

        // 뷰 찾기
        TextView usernameMessage = findViewById(R.id.usernameMessage);
        TextView studyStreak = findViewById(R.id.studyStreak);
        TextView correctAnswersTextView = findViewById(R.id.correctAnswersTextView);
        TextView averageTimeTextView = findViewById(R.id.averageTimeTextView);
        TextView totalQuestionsTextView = findViewById(R.id.totalQuestionsTextView);
        ImageButton thumbsUpButton = findViewById(R.id.thumbsUpButton);
        ImageButton thumbsDownButton = findViewById(R.id.thumbsDownButton);
        Button continueButton = findViewById(R.id.continueButton);

        // 데이터 가져오기
        String username = getUserName();
        int streakDays = getStudyStreakDays();
        int correctAnswers = getCorrectAnswers();
        int totalQuestions = getTotalQuestions();
        double averageTime = getAverageTime();
        int totalSolvedQuestions = getTotalSolvedQuestions();

        // 뷰 업데이트
        usernameMessage.setText("최고예요, " + username + "님!");
        studyStreak.setText("\uD83D\uDD25 " + streakDays + "일 연속 공부 중");
        correctAnswersTextView.setText(correctAnswers + "/" + totalQuestions);
        averageTimeTextView.setText(String.format("%.1f초", averageTime));
        totalQuestionsTextView.setText(String.valueOf(totalSolvedQuestions));

        // 버튼 리스너 설정
        thumbsUpButton.setOnClickListener(v -> {
            Toast.makeText(this, "좋아요를 선택하셨습니다!", Toast.LENGTH_SHORT).show();
            // 여기에 좋아요 처리 로직 추가
        });

        thumbsDownButton.setOnClickListener(v -> {
            Toast.makeText(this, "싫어요를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            // 여기에 싫어요 처리 로직 추가
        });

        continueButton.setOnClickListener(v -> {
            // 여기에 복습 화면으로 이동하는 로직 추가
            Toast.makeText(this, "복습 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
        });
    }

    private String getUserName() {
        return prefs.getString("username", "사용자");
    }

    private int getStudyStreakDays() {
        return prefs.getInt("streakDays", 1);
    }

    private int getCorrectAnswers() {
        return prefs.getInt("correctAnswers", 0);
    }

    private int getTotalQuestions() {
        return prefs.getInt("totalQuestions", 5);
    }

    private double getAverageTime() {
        return prefs.getFloat("averageTime", 30.0f);
    }

    private int getTotalSolvedQuestions() {
        return prefs.getInt("totalSolvedQuestions", 0);
    }
}