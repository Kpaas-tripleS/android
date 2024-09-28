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

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.R;
import com.example.android.quiz.viewmodel.QuizViewModel;
import com.example.android.quiz.dto.QuizDto;

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
                // Handle quiz result
                if (result.getIsCorrect()) {
                    currentQuizIndex++;
                    if (currentQuizIndex < quizzes.size()) {
                        displayQuiz(quizzes.get(currentQuizIndex));
                    } else {
                        // Quiz completed
                        // Navigate to result screen
                    }
                } else {
                    // Show error message
                }
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
    }

    private void submitAnswer() {
        int selectedId = answerRadioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedButton = findViewById(selectedId);
            String answer = selectedButton.getText().toString();
            viewModel.submitAnswer(quizzes.get(currentQuizIndex).getQuizId(), 1L, answer);
        }
    }

    // 타이머 종료 및 시간 저장
    private void stopTimerAndSave() {
        long endTime = SystemClock.elapsedRealtime();
        long solvingTime = (endTime - startTime) / 1000; // 밀리초를 초 단위로 변환
        solvingTimes.add(solvingTime);
    }

    private double calculateAverageTime() {  //QuizResultActivity로 전달
        long sum = 0;
        for (Long time : solvingTimes) {
            sum += time;
        }
        double average = (double) sum / solvingTimes.size(); // 평균을 double 타입으로 계산
        return Math.round(average * 10) / 10.0; // 소수점 한자리까지 반올림
    }

}