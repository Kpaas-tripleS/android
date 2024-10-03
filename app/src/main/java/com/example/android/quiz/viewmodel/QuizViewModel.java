package com.example.android.quiz.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.quiz.api.QuizApi;
import com.example.android.quiz.dto.QuizDto;
import com.example.android.quiz.dto.QuizAnswerDto;
import com.example.android.quiz.dto.QuizResultDto;
import com.example.android.global.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizViewModel extends AndroidViewModel {
    private QuizApi quizApi;
    private MutableLiveData<List<QuizDto>> quizzes = new MutableLiveData<>();
    private MutableLiveData<QuizResultDto> quizResult = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public QuizViewModel(Application application) {
        super(application);
        quizApi = RetrofitClient.getInstance(application.getApplicationContext()).getQuizApi();
    }

    public LiveData<List<QuizDto>> getQuizzes() {
        return quizzes;
    }

    public LiveData<QuizResultDto> getQuizResult() {
        return quizResult;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadQuizzes() {
        quizApi.getRandomQuizzes().enqueue(new Callback<List<QuizDto>>() {
            @Override
            public void onResponse(Call<List<QuizDto>> call, Response<List<QuizDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    quizzes.setValue(response.body());
                } else {
                    error.setValue("퀴즈 로딩 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<QuizDto>> call, Throwable t) {
                handleError(t);
            }
        });
    }

    public void submitAnswer(Long quizId,String answer) {
        QuizAnswerDto answerDto = new QuizAnswerDto();
        answerDto.setAnswer(answer);

        quizApi.submitAnswer(quizId, answerDto).enqueue(new Callback<QuizResultDto>() {
            @Override
            public void onResponse(Call<QuizResultDto> call, Response<QuizResultDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    quizResult.setValue(response.body());
                } else {
                    error.setValue("답변 제출 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<QuizResultDto> call, Throwable t) {
                handleError(t);
            }
        });
    }

    private void handleError(Throwable t) {
        error.setValue("오류 발생: " + t.getMessage());
        // 로그에 에러 기록
        Log.e("QuizViewModel", "API 호출 실패", t);
    }
}