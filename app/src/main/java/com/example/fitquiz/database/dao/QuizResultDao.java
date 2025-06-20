package com.example.fitquiz.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.fitquiz.database.entities.QuizResult;
import java.util.List;

@Dao
public interface QuizResultDao {
    @Insert
    void insert(QuizResult quizResult);
    
    @Query("SELECT * FROM quiz_results ORDER BY date DESC")
    List<QuizResult> getAllResults();
    
    @Query("SELECT * FROM quiz_results WHERE date >= :startDate ORDER BY date DESC")
    List<QuizResult> getResultsFromDate(long startDate);
    
    @Query("SELECT AVG(score) FROM quiz_results WHERE date >= :startDate")
    double getAverageScore(long startDate);
}
