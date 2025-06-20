package com.example.fitquiz.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "user_progress")
public class UserProgress {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public Date date;
    public int dailyScore;
    public boolean quizCompleted;
    public boolean challengeCompleted;
    public int streak;
    
    public UserProgress(Date date, int dailyScore, boolean quizCompleted, boolean challengeCompleted, int streak) {
        this.date = date;
        this.dailyScore = dailyScore;
        this.quizCompleted = quizCompleted;
        this.challengeCompleted = challengeCompleted;
        this.streak = streak;
    }
    
    // Getters
    public int getId() { return id; }
    public Date getDate() { return date; }
    public int getDailyScore() { return dailyScore; }
    public boolean isQuizCompleted() { return quizCompleted; }
    public boolean isChallengeCompleted() { return challengeCompleted; }
    public int getStreak() { return streak; }
}
