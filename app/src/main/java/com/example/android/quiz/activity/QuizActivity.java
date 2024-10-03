package com.example.android.quiz.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.R;
import com.example.android.quiz.viewmodel.QuizViewModel;
import com.example.android.quiz.dto.QuizDto;
import com.example.android.quiz.dto.QuizResultDto;

import java.util.ArrayList;
import java.util.List;


public class QuizActivity extends AppCompatActivity {
    private QuizViewModel viewModel;
    private TextView questionText;
    private RadioGroup answerRadioGroup;
    private Button submitButton;
    private List<QuizDto> quizzes;
    private int currentQuizIndex = 0;
    private long startTime;
    private List<Long> solvingTimes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        questionText = findViewById(R.id.question_text);
        answerRadioGroup = findViewById(R.id.answerRadioGroup);
        submitButton = findViewById(R.id.submitButton);

        viewModel.getQuizzes().observe(this, quizList -> {
            if (quizList != null && !quizList.isEmpty()) {
                quizzes = quizList;
                displayQuiz(quizzes.get(currentQuizIndex));
            }
        });

        viewModel.getQuizResult().observe(this, result -> {
            if (result != null) {
                handleQuizResult(result);
            }
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        submitButton.setOnClickListener(v -> submitAnswer());

        viewModel.loadQuizzes();

        Log.d("QuizActivity", "onCreate called");
        Toast.makeText(this, "퀴즈 액티비티 시작", Toast.LENGTH_SHORT).show();
    }

    private void displayQuiz(QuizDto quiz) {
        questionText.setText(quiz.getQuestion());
        ((RadioButton)answerRadioGroup.getChildAt(0)).setText(quiz.getChoiceOne());
        ((RadioButton)answerRadioGroup.getChildAt(1)).setText(quiz.getChoiceTwo());
        ((RadioButton)answerRadioGroup.getChildAt(2)).setText(quiz.getChoiceThree());
        ((RadioButton)answerRadioGroup.getChildAt(3)).setText(quiz.getChoiceFour());
        answerRadioGroup.clearCheck();
        startTimer();
    }

    private void submitAnswer() {
        int selectedId = answerRadioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedButton = findViewById(selectedId);
            String answer = selectedButton.getText().toString();
            viewModel.submitAnswer(quizzes.get(currentQuizIndex).getQuizId(), answer);
            stopTimerAndSave();
        } else {
            Toast.makeText(this, "답변을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleQuizResult(QuizResultDto result) {
        if (result.getIsCorrect()) {
            currentQuizIndex++;
            if (currentQuizIndex < quizzes.size()) {
                displayQuiz(quizzes.get(currentQuizIndex));
            } else {
                finishQuiz();
            }
        } else {
            Toast.makeText(this, "틀렸습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer() {
        startTime = SystemClock.elapsedRealtime();
    }

    private void stopTimerAndSave() {
        long endTime = SystemClock.elapsedRealtime();
        long solvingTime = (endTime - startTime) / 1000; // 밀리초를 초 단위로 변환
        solvingTimes.add(solvingTime);
    }

    private double calculateAverageTime() {
        if (solvingTimes.isEmpty()) return 0;
        long sum = 0;
        for (Long time : solvingTimes) {
            sum += time;
        }
        double average = (double) sum / solvingTimes.size();
        return Math.round(average * 10) / 10.0; // 소수점 한자리까지 반올림
    }

    private void finishQuiz() {
        double averageTime = calculateAverageTime();
        // 여기에 QuizResultActivity로 이동하는 코드 추가
        Intent intent = new Intent(this, QuizResultActivity.class);
        intent.putExtra("averageTime", (float)averageTime);
        intent.putExtra("correctAnswers", (int)currentQuizIndex);
        intent.putExtra("totalQuestions", quizzes.size());
        startActivity(intent);
        finish();
    }
}