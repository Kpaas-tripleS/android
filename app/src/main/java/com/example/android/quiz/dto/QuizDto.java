package com.example.android.quiz.dto;


public class QuizDto {
    private Long quizId;
    private String question;
    private String choiceOne;
    private String choiceTwo;
    private String choiceThree;
    private String choiceFour;

    // Getters and setters
    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getChoiceOne() { return choiceOne; }
    public void setChoiceOne(String choiceOne) { this.choiceOne = choiceOne; }
    public String getChoiceTwo() { return choiceTwo; }
    public void setChoiceTwo(String choiceTwo) { this.choiceTwo = choiceTwo; }
    public String getChoiceThree() { return choiceThree; }
    public void setChoiceThree(String choiceThree) { this.choiceThree = choiceThree; }
    public String getChoiceFour() { return choiceFour; }
    public void setChoiceFour(String choiceFour) { this.choiceFour = choiceFour; }
}
