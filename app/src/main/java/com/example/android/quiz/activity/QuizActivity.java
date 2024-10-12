package com.example.android.quiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.quiz.dto.QuizDto;
import com.example.android.quiz.dto.QuizResultDto;
import com.example.android.quiz.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private QuizViewModel viewModel;
    private TextView questionText;
    private RadioGroup answerRadioGroup;
    private Button submitButton;
    private TextView timerTextView;
    private TextView progressTextView;
    private ProgressBar progressBar;
    private List<QuizDto> quizzes;
    private int currentQuizIndex = 0;
    private List<Integer> solvingTimes = new ArrayList<>();
    private int elapsedTime = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        questionText = findViewById(R.id.question_text);
        answerRadioGroup = findViewById(R.id.answerRadioGroup);
        submitButton = findViewById(R.id.submitButton);
        timerTextView = findViewById(R.id.timerTextView);
        progressTextView = findViewById(R.id.progressTextView);
        progressBar = findViewById(R.id.progressBar);

        viewModel.getQuizzes().observe(this, quizList -> {
            if (quizList != null && !quizList.isEmpty()) {
                quizzes = quizList;
                progressBar.setMax(quizzes.size());
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

        startTimer();
    }

    private void displayQuiz(QuizDto quiz) {
        questionText.setText(quiz.getQuestion());
        ((RadioButton)answerRadioGroup.getChildAt(0)).setText(quiz.getChoiceOne());
        ((RadioButton)answerRadioGroup.getChildAt(1)).setText(quiz.getChoiceTwo());
        ((RadioButton)answerRadioGroup.getChildAt(2)).setText(quiz.getChoiceThree());
        ((RadioButton)answerRadioGroup.getChildAt(3)).setText(quiz.getChoiceFour());
        answerRadioGroup.clearCheck();

        currentQuizIndex++;
        updateProgress();
    }

    private void updateProgress() {
        progressTextView.setText(currentQuizIndex + "/" + quizzes.size());
        progressBar.setProgress(currentQuizIndex);
    }

    private void startTimer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                elapsedTime++;
                timerTextView.setText(elapsedTime + "초");
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void submitAnswer() {
        int selectedId = answerRadioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedButton = findViewById(selectedId);
            String answer = selectedButton.getText().toString();
            viewModel.submitAnswer(quizzes.get(currentQuizIndex - 1).getQuizId(), answer);
            solvingTimes.add(elapsedTime);
            elapsedTime = 0; // Reset timer for next question
        } else {
            Toast.makeText(this, "답변을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleQuizResult(QuizResultDto result) {
        if (result.getIsCorrect()) {
            if (currentQuizIndex < quizzes.size()) {
                displayQuiz(quizzes.get(currentQuizIndex));
            } else {
                finishQuiz();
            }
        } else {
            Toast.makeText(this, "틀렸습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateAverageTime() {
        if (solvingTimes.isEmpty()) return 0;
        int sum = 0;
        for (Integer time : solvingTimes) {
            sum += time;
        }
        return (double) sum / solvingTimes.size();
    }

    private void finishQuiz() {
        handler.removeCallbacksAndMessages(null);
        double averageTime = calculateAverageTime();
        Intent intent = new Intent(this, QuizResultActivity.class);
        intent.putExtra("averageTime", (float)averageTime);
        intent.putExtra("correctAnswers", currentQuizIndex);
        intent.putExtra("totalQuestions", quizzes.size());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}