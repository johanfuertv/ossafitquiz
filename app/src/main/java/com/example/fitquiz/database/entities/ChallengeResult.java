package com.example.fitquiz.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "challenge_results")
public class ChallengeResult {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String challengeType;
    public int targetReps;
    public int completedReps;
    public boolean completed;
    public Date date;
    
    public ChallengeResult(String challengeType, int targetReps, int completedReps, boolean completed, Date date) {
        this.challengeType = challengeType;
        this.targetReps = targetReps;
        this.completedReps = completedReps;
        this.completed = completed;
        this.date = date;
    }
    
    // Getters
    public int getId() { return id; }
    public String getChallengeType() { return challengeType; }
    public int getTargetReps() { return targetReps; }
    public int getCompletedReps() { return completedReps; }
    public boolean isCompleted() { return completed; }
    public Date getDate() { return date; }
}
