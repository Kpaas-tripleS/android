package com.example.android.review.api;

import com.example.android.review.dto.ReviewDto;
import com.example.android.quiz.dto.QuizAnswerDto;

import retrofit2.Call;
import retrofit2.http.*;

public interface ReviewApi {
    @POST("/reviews/user/random-session")
    Call<ReviewDto> createRandomReviewSession(@Query("difficulty") String difficulty);

    @POST("/reviews/quiz/{quizId}/submit")
    Call<Void> submitReviewAnswer(@Path("quizId") Long quizId, @Body QuizAnswerDto answerDto);
}