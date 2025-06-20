package com.example.fitquiz.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.example.fitquiz.database.dao.QuizResultDao;
import com.example.fitquiz.database.dao.ChallengeResultDao;
import com.example.fitquiz.database.dao.UserProgressDao;
import com.example.fitquiz.database.entities.QuizResult;
import com.example.fitquiz.database.entities.ChallengeResult;
import com.example.fitquiz.database.entities.UserProgress;

@Database(
    entities = {QuizResult.class, ChallengeResult.class, UserProgress.class},
    version = 1,
    exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    
    private static volatile AppDatabase INSTANCE;
    
    public abstract QuizResultDao quizResultDao();
    public abstract ChallengeResultDao challengeResultDao();
    public abstract UserProgressDao userProgressDao();
    
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "fitquiz_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
