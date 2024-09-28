package com.example.android.quiz.dto;

import java.time.LocalDateTime;

public class QuizResultDto {
    private Long quizId;
    private Long userId;
    private String userAnswer;
    private Boolean isCorrect;
    private LocalDateTime answeredAt;
    private Long resultId;

    // Getters and setters
    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean correct) { isCorrect = correct; }
    public LocalDateTime getAnsweredAt() { return answeredAt; }
    public void setAnsweredAt(LocalDateTime answeredAt) { this.answeredAt = answeredAt; }
    public Long getResultId() { return resultId; }
    public void setResultId(Long resultId) { this.resultId = resultId; }

}
