package com.example.android.review.dto;

import com.example.android.quiz.dto.QuizResultDto;
import java.time.LocalDateTime;
import java.util.List;

public class ReviewDto {
    private Long id;
    private Long userId;
    private Long quizId;
    private LocalDateTime reviewedAt;
    private long wrongCount;
    private boolean isReviewed;
    private List<QuizResultDto> quizResults;
    private String difficultyLevel;

    // Getters
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public long getWrongCount() {
        return wrongCount;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public List<QuizResultDto> getQuizResults() {
        return quizResults;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public void setWrongCount(long wrongCount) {
        this.wrongCount = wrongCount;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    public void setQuizResults(List<QuizResultDto> quizResults) {
        this.quizResults = quizResults;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}