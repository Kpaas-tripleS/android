package com.example.android.review.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.android.global.RetrofitClient;
import com.example.android.review.api.ReviewApi;
import com.example.android.review.dto.ReviewDto;
import com.example.android.quiz.dto.QuizAnswerDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewViewModel extends AndroidViewModel {
    private ReviewApi reviewApi;
    private MutableLiveData<ReviewDto> reviewSession = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public ReviewViewModel(Application application) {
        super(application);
        reviewApi = RetrofitClient.getInstance(application).getReviewApi();
    }

    public LiveData<ReviewDto> getReviewSession() {
        return reviewSession;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void createRandomReviewSession(String difficulty) {
        reviewApi.createRandomReviewSession(difficulty).enqueue(new Callback<ReviewDto>() {
            @Override
            public void onResponse(Call<ReviewDto> call, Response<ReviewDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewSession.setValue(response.body());
                } else {
                    error.setValue("리뷰 세션 생성 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ReviewDto> call, Throwable t) {
                error.setValue("네트워크 오류: " + t.getMessage());
            }
        });
    }

    public void submitReviewAnswer(Long quizId, String answer) {
        QuizAnswerDto answerDto = new QuizAnswerDto();
        answerDto.setUserId(getUserId());
        answerDto.setAnswer(answer);

        reviewApi.submitReviewAnswer(quizId, answerDto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 답변 제출 성공 처리
                    // 예: 다음 문제로 넘어가거나, 리뷰 세션 업데이트
                    updateReviewSession();
                } else {
                    error.setValue("답변 제출 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                error.setValue("네트워크 오류: " + t.getMessage());
            }
        });
    }

    private Long getUserId() {
        return RetrofitClient.getInstance(getApplication()).getUserId(getApplication());
    }

    private void updateReviewSession() {
        // 현재 리뷰 세션의 상태를 업데이트하는 로직
        // 예: 다음 문제로 넘어가기, 점수 업데이트 등
        ReviewDto currentSession = reviewSession.getValue();
        if (currentSession != null) {
            // 여기에 업데이트 로직 구현
            // 예: currentSession.moveToNextQuestion();
            reviewSession.setValue(currentSession);
        }
    }
}