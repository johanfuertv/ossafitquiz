package com.example.fitquiz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.fitquiz.ChallengeActivity;
import com.example.fitquiz.ProgressActivity;
import com.example.fitquiz.QuizActivity;
import com.example.fitquiz.R;
import com.example.fitquiz.database.AppDatabase;
import com.example.fitquiz.database.entities.UserProgress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    
    private AppDatabase database;
    private TextView welcomeText;
    private TextView dateText;
    private TextView streakText;
    private ProgressBar dailyProgress;
    private TextView progressText;
    private Button quizButton;
    private Button challengeButton;
    private Button progressButton;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        database = AppDatabase.getDatabase(getContext());
        
        initializeViews(view);
        setupClickListeners();
        updateDailyStatus();
        
        return view;
    }
    
    private void initializeViews(View view) {
        welcomeText = view.findViewById(R.id.welcome_text);
        dateText = view.findViewById(R.id.date_text);
        streakText = view.findViewById(R.id.streak_text);
        dailyProgress = view.findViewById(R.id.daily_progress);
        progressText = view.findViewById(R.id.progress_text);
        quizButton = view.findViewById(R.id.quiz_button);
        challengeButton = view.findViewById(R.id.challenge_button);
        progressButton = view.findViewById(R.id.progress_button);
        

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
        dateText.setText(dateFormat.format(new Date()));
        

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour < 12) {
            greeting = "¡Buenos días!";
        } else if (hour < 18) {
            greeting = "¡Buenas tardes!";
        } else {
            greeting = "¡Buenas noches!";
        }
        welcomeText.setText(greeting);
    }
    
    private void setupClickListeners() {
        quizButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), QuizActivity.class);
            startActivity(intent);
        });
        
        challengeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChallengeActivity.class);
            startActivity(intent);
        });
        
        progressButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProgressActivity.class);
            startActivity(intent);
        });
    }
    
    private void updateDailyStatus() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        Date today = calendar.getTime();
        UserProgress todayProgress = database.userProgressDao().getProgressForDate(today.getTime());
        
        if (todayProgress != null) {
            // Update streak
            streakText.setText(String.format("Racha: %d días", todayProgress.getStreak()));
            
            // Update daily progress
            int progress = 0;
            if (todayProgress.isQuizCompleted()) progress += 50;
            if (todayProgress.isChallengeCompleted()) progress += 50;
            
            dailyProgress.setProgress(progress);
            progressText.setText(String.format("Progreso diario: %d%%", progress));
            
            // Update button states
            if (todayProgress.isQuizCompleted()) {
                quizButton.setText("Quiz Completado ✓");
                quizButton.setEnabled(false);
            }
            
            if (todayProgress.isChallengeCompleted()) {
                challengeButton.setText("Reto Completado ✓");
                challengeButton.setEnabled(false);
            }
        } else {
            streakText.setText("Racha: 0 días");
            dailyProgress.setProgress(0);
            progressText.setText("Progreso diario: 0%");
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        updateDailyStatus();
    }
}
