package com.example.android.quiz.api;

import com.example.android.quiz.dto.QuizDto;
import com.example.android.quiz.dto.QuizAnswerDto;
import com.example.android.quiz.dto.QuizResultDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface QuizApi {
    @GET("quiz")
    Call<List<QuizDto>> getRandomQuizzes();

    @GET("quiz/{quizId}")
    Call<QuizDto> getQuiz(@Path("quizId") Long quizId);

    @POST("quiz/{quizId}/answer")
    Call<QuizResultDto> submitAnswer(@Path("quizId") Long quizId, @Body QuizAnswerDto answerDto);

    @GET("quiz/{quizId}/answer")
    Call<QuizAnswerDto> getQuizAnswer(@Path("quizId") Long quizId);
}
