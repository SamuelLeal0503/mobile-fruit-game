package com.example.myapplication;

public class User {
    private String username;
    private int score;

    public User() {
        // Empty constructor needed for Firestore
    }

    public User(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
}

