package com.example.fitquiz.models;

import java.util.List;

public class Question {
    private String question;
    private List<String> options;
    private int correctAnswer;
    private String explanation;
    
    public Question(String question, List<String> options, int correctAnswer, String explanation) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }
    
    // Getters
    public String getQuestion() { return question; }
    public List<String> getOptions() { return options; }
    public int getCorrectAnswer() { return correctAnswer; }
    public String getExplanation() { return explanation; }
}
