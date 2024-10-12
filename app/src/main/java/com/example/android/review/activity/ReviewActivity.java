package com.example.android.review.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.review.viewmodel.ReviewViewModel;
import com.example.android.review.dto.ReviewDto;
import com.example.android.quiz.dto.QuizResultDto;
import com.example.android.quiz.dto.QuizDto;

public class ReviewActivity extends AppCompatActivity {
    private ReviewViewModel viewModel;
    private TextView questionText;
    private RadioGroup answerRadioGroup;
    private Button submitButton;
    private TextView wrongCountText;
    private int currentQuizIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        viewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        questionText = findViewById(R.id.question_text);
        answerRadioGroup = findViewById(R.id.answerRadioGroup);
        submitButton = findViewById(R.id.submitButton);
        wrongCountText = findViewById(R.id.wrongCountText);

        viewModel.getReviewSession().observe(this, this::updateUI);
        viewModel.getError().observe(this, error -> Toast.makeText(this, error, Toast.LENGTH_LONG).show());

        submitButton.setOnClickListener(v -> submitAnswer());

        viewModel.createRandomReviewSession("RANDOM");
    }

    private void updateUI(ReviewDto reviewDto) {
        if (reviewDto != null && reviewDto.getQuizResults() != null && !reviewDto.getQuizResults().isEmpty()) {
            QuizResultDto currentQuizResult = reviewDto.getQuizResults().get(currentQuizIndex);
            if (currentQuizResult != null) {
                QuizDto currentQuiz = currentQuizResult.getQuiz();
                if (currentQuiz != null) {
                    questionText.setText(currentQuiz.getQuestion());

                    ((RadioButton)answerRadioGroup.getChildAt(0)).setText(currentQuiz.getChoiceOne());
                    ((RadioButton)answerRadioGroup.getChildAt(1)).setText(currentQuiz.getChoiceTwo());
                    ((RadioButton)answerRadioGroup.getChildAt(2)).setText(currentQuiz.getChoiceThree());
                    ((RadioButton)answerRadioGroup.getChildAt(3)).setText(currentQuiz.getChoiceFour());

                    wrongCountText.setText(String.format("지금까지 틀린 횟수: %d", currentQuizResult.getWrongCount()));
                }
            }
        }
    }

    private void submitAnswer() {
        int selectedId = answerRadioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedButton = findViewById(selectedId);
            String answer = selectedButton.getText().toString();
            ReviewDto reviewSession = viewModel.getReviewSession().getValue();
            if (reviewSession != null && !reviewSession.getQuizResults().isEmpty()) {
                Long currentQuizId = reviewSession.getQuizResults().get(currentQuizIndex).getQuizId();
                viewModel.submitReviewAnswer(currentQuizId, answer);
            }
        } else {
            Toast.makeText(this, "답변을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}