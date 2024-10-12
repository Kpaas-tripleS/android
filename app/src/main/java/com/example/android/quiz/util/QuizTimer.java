package com.example.android.quiz.util;

public class QuizTimer {
    private long startTime;

    public void start() {
        startTime = System.currentTimeMillis() / 1000; // 초 단위로 저장
    }

    public int getElapsedTime() {
        long currentTime = System.currentTimeMillis() / 1000;
        return (int)(currentTime - startTime);
    }

    public void reset() {
        startTime = System.currentTimeMillis() / 1000;
    }
}