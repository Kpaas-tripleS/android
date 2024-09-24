package com.example.android.quiz.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.android.quiz.api.QuizApi;
import com.example.android.quiz.dto.QuizDto;
import com.example.android.quiz.dto.QuizAnswerDto;
import com.example.android.quiz.dto.QuizResultDto;
import com.example.android.global.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class QuizViewModel extends ViewModel {
    private QuizApi quizApi;
    private MutableLiveData<List<QuizDto>> quizzes = new MutableLiveData<>();
    private MutableLiveData<QuizResultDto> quizResult = new MutableLiveData<>();

    public QuizViewModel() {
        quizApi = RetrofitClient.getInstance().getQuizApi();
    }

    public LiveData<List<QuizDto>> getQuizzes() {
        return quizzes;
    }

    public LiveData<QuizResultDto> getQuizResult() {
        return quizResult;
    }

    public void loadQuizzes() {
        quizApi.getRandomQuizzes().enqueue(new Callback<List<QuizDto>>() {
            @Override
            public void onResponse(Call<List<QuizDto>> call, Response<List<QuizDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    quizzes.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<QuizDto>> call, Throwable t) {
                // Handle error
            }
        });
    }

    public void submitAnswer(Long quizId, Long userId, String answer) {
        QuizAnswerDto answerDto = new QuizAnswerDto();
        answerDto.setUserId(userId);
        answerDto.setAnswer(answer);

        quizApi.submitAnswer(quizId, answerDto).enqueue(new Callback<QuizResultDto>() {
            @Override
            public void onResponse(Call<QuizResultDto> call, Response<QuizResultDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    quizResult.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<QuizResultDto> call, Throwable t) {
                // Handle error
            }
        });
    }
}