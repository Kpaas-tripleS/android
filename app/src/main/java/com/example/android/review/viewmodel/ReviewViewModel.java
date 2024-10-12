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
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

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

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void createRandomReviewSession(String difficulty) {
        isLoading.setValue(true);
        reviewApi.createRandomReviewSession(difficulty).enqueue(new Callback<ReviewDto>() {
            @Override
            public void onResponse(Call<ReviewDto> call, Response<ReviewDto> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    reviewSession.setValue(response.body());
                } else {
                    error.setValue("리뷰 세션 생성 실패: " + response.code());
                    reviewSession.setValue(new ReviewDto()); // 빈 ReviewDto 설정
                }
            }

            @Override
            public void onFailure(Call<ReviewDto> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue("네트워크 오류: " + t.getMessage());
                reviewSession.setValue(new ReviewDto()); // 빈 ReviewDto 설정
            }
        });
    }

    public void submitReviewAnswer(Long quizId, String answer) {
        isLoading.setValue(true);
        QuizAnswerDto answerDto = new QuizAnswerDto();
        answerDto.setUserId(getUserId());
        answerDto.setAnswer(answer);

        reviewApi.submitReviewAnswer(quizId, answerDto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    updateReviewSession();
                } else {
                    error.setValue("답변 제출 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue("네트워크 오류: " + t.getMessage());
            }
        });
    }

    private Long getUserId() {
        return RetrofitClient.getInstance(getApplication()).getUserId(getApplication());
    }

    private void updateReviewSession() {
        ReviewDto currentSession = reviewSession.getValue();
        if (currentSession != null && currentSession.getQuizResults() != null) {
            int currentIndex = getCurrentQuestionIndex();
            if (currentIndex < currentSession.getQuizResults().size() - 1) {
                // 다음 문제로 이동
                currentIndex++;
            } else {
                // 모든 문제를 다 풀었을 경우
                error.setValue("모든 문제를 다 풀었습니다.");
            }
            reviewSession.setValue(currentSession); // UI 업데이트를 위해 setValue 호출
        }
    }

    private int getCurrentQuestionIndex() {
        ReviewDto currentSession = reviewSession.getValue();
        if (currentSession != null && currentSession.getQuizResults() != null) {
            for (int i = 0; i < currentSession.getQuizResults().size(); i++) {
                if (currentSession.getQuizResults().get(i).getUserAnswer() == null) {
                    return i;
                }
            }
        }
        return 0; // 기본값으로 첫 번째 문제 반환
    }
}