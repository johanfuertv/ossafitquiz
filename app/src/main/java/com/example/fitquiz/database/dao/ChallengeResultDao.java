package com.example.fitquiz.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.fitquiz.database.entities.ChallengeResult;
import java.util.List;

@Dao
public interface ChallengeResultDao {
    @Insert
    void insert(ChallengeResult challengeResult);
    
    @Query("SELECT * FROM challenge_results ORDER BY date DESC")
    List<ChallengeResult> getAllResults();
    
    @Query("SELECT * FROM challenge_results WHERE date >= :startDate ORDER BY date DESC")
    List<ChallengeResult> getResultsFromDate(long startDate);
    
    @Query("SELECT COUNT(*) FROM challenge_results WHERE completed = 1 AND date >= :startDate")
    int getCompletedChallengesCount(long startDate);
}
