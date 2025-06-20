package com.example.fitquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitquiz.R;
import com.example.fitquiz.adapters.AchievementAdapter;
import com.example.fitquiz.database.AppDatabase;
import com.example.fitquiz.models.Achievement;
import com.example.fitquiz.utils.SoundEffects;
import java.util.ArrayList;
import java.util.List;

public class AchievementsFragment extends Fragment {
    
    private RecyclerView achievementsRecyclerView;
    private TextView levelText;
    private TextView experienceText;
    private ImageView levelBadge;
    private SoundEffects soundEffects;
    private AppDatabase database;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);
        
        database = AppDatabase.getDatabase(getContext());
        soundEffects = new SoundEffects(getContext());
        
        initializeViews(view);
        loadAchievements();
        loadUserLevel();
        
        return view;
    }
    
    private void initializeViews(View view) {
        achievementsRecyclerView = view.findViewById(R.id.achievements_recycler_view);
        levelText = view.findViewById(R.id.level_text);
        experienceText = view.findViewById(R.id.experience_text);
        levelBadge = view.findViewById(R.id.level_badge);
        
        achievementsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    
    private void loadAchievements() {
        List<Achievement> achievements = new ArrayList<>();
        
        // Obtener datos de logros desde la base de datos o crear ejemplos
        int totalQuizzes = database.quizResultDao().getAllResults().size();
        int totalChallenges = database.challengeResultDao().getAllResults().size();
        int maxStreak = database.userProgressDao().getMaxStreak();
        
        // Logros de Quiz
        achievements.add(new Achievement(
            getString(R.string.quiz_beginner),
            getString(R.string.complete_5_quizzes),
            totalQuizzes >= 5,
            totalQuizzes + "/5",
            R.drawable.ic_achievement_quiz
        ));
        
        achievements.add(new Achievement(
            getString(R.string.quiz_master),
            getString(R.string.complete_20_quizzes),
            totalQuizzes >= 20,
            totalQuizzes + "/20",
            R.drawable.ic_achievement_quiz_gold
        ));
        
        // Logros de Retos
        achievements.add(new Achievement(
            getString(R.string.challenge_beginner),
            getString(R.string.complete_5_challenges),
            totalChallenges >= 5,
            totalChallenges + "/5",
            R.drawable.ic_achievement_challenge
        ));
        
        achievements.add(new Achievement(
            getString(R.string.challenge_master),
            getString(R.string.complete_20_challenges),
            totalChallenges >= 20,
            totalChallenges + "/20",
            R.drawable.ic_achievement_challenge_gold
        ));
        
        // Logros de Racha
        achievements.add(new Achievement(
            getString(R.string.streak_beginner),
            getString(R.string.maintain_3_day_streak),
            maxStreak >= 3,
            maxStreak + "/3",
            R.drawable.ic_achievement_streak
        ));
        
        achievements.add(new Achievement(
            getString(R.string.streak_master),
            getString(R.string.maintain_7_day_streak),
            maxStreak >= 7,
            maxStreak + "/7",
            R.drawable.ic_achievement_streak_gold
        ));
        
        // Configurar adaptador
        AchievementAdapter adapter = new AchievementAdapter(achievements);
        achievementsRecyclerView.setAdapter(adapter);
    }
    
    private void loadUserLevel() {
        // Calcular nivel basado en puntos totales
        int totalPoints = calculateTotalPoints();
        int level = totalPoints / 100 + 1; // Cada 100 puntos sube un nivel
        int experience = totalPoints % 100; // Puntos restantes para el siguiente nivel
        
        levelText.setText(String.format(getString(R.string.level_format), level));
        experienceText.setText(String.format(getString(R.string.experience_format), experience, 100));
        
        // Seleccionar insignia según el nivel
        int badgeResource;
        if (level >= 10) {
            badgeResource = R.drawable.ic_badge_gold;
        } else if (level >= 5) {
            badgeResource = R.drawable.ic_badge_silver;
        } else {
            badgeResource = R.drawable.ic_badge_bronze;
        }
        
        levelBadge.setImageResource(badgeResource);
        
        // Animar insignia
        Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_animation);
        levelBadge.startAnimation(rotate);
    }
    
    private int calculateTotalPoints() {
        int totalPoints = 0;
        
        // Sumar puntos de todos los progresos diarios
        List<com.example.fitquiz.database.entities.UserProgress> progressList = 
            database.userProgressDao().getAllProgress();
        
        for (com.example.fitquiz.database.entities.UserProgress progress : progressList) {
            totalPoints += progress.getDailyScore();
        }
        
        return totalPoints;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (soundEffects != null) {
            soundEffects.release();
        }
    }
}
