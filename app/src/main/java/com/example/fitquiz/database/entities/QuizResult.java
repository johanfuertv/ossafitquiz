package com.example.fitquiz.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "quiz_results")
public class QuizResult {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public int score;
    public int totalQuestions;
    public Date date;
    public long timeSpent; // in milliseconds
    
    public QuizResult(int score, int totalQuestions, Date date, long timeSpent) {
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.date = date;
        this.timeSpent = timeSpent;
    }
    
    // Getters
    public int getId() { return id; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public Date getDate() { return date; }
    public long getTimeSpent() { return timeSpent; }
}
