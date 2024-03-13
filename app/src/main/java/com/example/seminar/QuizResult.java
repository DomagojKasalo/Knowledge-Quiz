package com.example.seminar;

public class QuizResult {
    private String userId;
    private int score;
    private long timestamp;

    public QuizResult() {
    }

    public QuizResult(String userId, int score) {
        this.userId = userId;
        this.score = score;
        this.timestamp = System.currentTimeMillis();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
