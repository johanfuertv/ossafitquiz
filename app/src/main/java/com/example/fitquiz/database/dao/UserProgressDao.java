package com.example.fitquiz.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.fitquiz.database.entities.UserProgress;
import java.util.List;

@Dao
public interface UserProgressDao {
    @Insert
    void insert(UserProgress userProgress);
    
    @Update
    void update(UserProgress userProgress);
    
    @Query("SELECT * FROM user_progress ORDER BY date DESC")
    List<UserProgress> getAllProgress();
    
    @Query("SELECT * FROM user_progress WHERE date >= :startDate ORDER BY date DESC")
    List<UserProgress> getProgressFromDate(long startDate);
    
    @Query("SELECT * FROM user_progress WHERE date = :date LIMIT 1")
    UserProgress getProgressForDate(long date);
    
    @Query("SELECT MAX(streak) FROM user_progress")
    int getMaxStreak();
}
