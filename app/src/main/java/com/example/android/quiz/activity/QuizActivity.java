package com.example.android.quiz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.R;
import com.example.android.quiz.viewmodel.QuizViewModel;
import com.example.android.quiz.dto.QuizDto;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private QuizViewModel viewModel;
    private TextView questionText;
    private RadioGroup answerRadioGroup;
    private Button submitButton;
    private List<QuizDto> quizzes;
    private int currentQuizIndex = 0;

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
}