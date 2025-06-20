package com.example.fitquiz.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.fitquiz.R;
import com.example.fitquiz.database.AppDatabase;
import com.example.fitquiz.database.entities.QuizResult;
import com.example.fitquiz.database.entities.ChallengeResult;
import com.example.fitquiz.database.entities.UserProgress;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.components.XAxis;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatsFragment extends Fragment {
    
    private AppDatabase database;
    private TextView totalQuizzesText;
    private TextView averageScoreText;
    private TextView totalChallengesText;
    private TextView maxStreakText;
    private LineChart progressChart;
    private BarChart weeklyChart;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        
        database = AppDatabase.getDatabase(getContext());
        
        initializeViews(view);
        loadStatistics();
        setupCharts();
        
        return view;
    }
    
    private void initializeViews(View view) {
        totalQuizzesText = view.findViewById(R.id.total_quizzes_text);
        averageScoreText = view.findViewById(R.id.average_score_text);
        totalChallengesText = view.findViewById(R.id.total_challenges_text);
        maxStreakText = view.findViewById(R.id.max_streak_text);
        progressChart = view.findViewById(R.id.progress_chart);
        weeklyChart = view.findViewById(R.id.weekly_chart);
    }
    
    private void loadStatistics() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        long weekStart = calendar.getTimeInMillis();
        

        List<QuizResult> quizResults = database.quizResultDao().getResultsFromDate(weekStart);
        totalQuizzesText.setText(String.valueOf(quizResults.size()));
        
        if (!quizResults.isEmpty()) {
            double averageScore = database.quizResultDao().getAverageScore(weekStart);
            averageScoreText.setText(String.format("%.1f%%", averageScore * 20)); // Convert to percentage
        } else {
            averageScoreText.setText("0%");
        }
        

        int completedChallenges = database.challengeResultDao().getCompletedChallengesCount(weekStart);
        totalChallengesText.setText(String.valueOf(completedChallenges));
        

        int maxStreak = database.userProgressDao().getMaxStreak();
        maxStreakText.setText(String.valueOf(maxStreak));
    }
    
    private void setupCharts() {
        setupProgressChart();
        setupWeeklyChart();
    }
    
    private void setupProgressChart() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        long weekStart = calendar.getTimeInMillis();
        
        List<UserProgress> progressList = database.userProgressDao().getProgressFromDate(weekStart);
        
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < progressList.size(); i++) {
            UserProgress progress = progressList.get(i);
            entries.add(new Entry(i, progress.getDailyScore()));
        }
        
        LineDataSet dataSet = new LineDataSet(entries, "Puntuación Diaria");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextSize(10f);
        
        LineData lineData = new LineData(dataSet);
        progressChart.setData(lineData);
        progressChart.getDescription().setText("Progreso de la Semana");
        progressChart.invalidate();
    }
    
    private void setupWeeklyChart() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        
        ArrayList<BarEntry> quizEntries = new ArrayList<>();
        ArrayList<BarEntry> challengeEntries = new ArrayList<>();
        
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            long dayStart = calendar.getTimeInMillis();
            
            // Count quizzes for this day
            List<QuizResult> dayQuizzes = database.quizResultDao().getResultsFromDate(dayStart);
            quizEntries.add(new BarEntry(i, dayQuizzes.size()));
            
            // Count challenges for this day
            int dayChallenges = database.challengeResultDao().getCompletedChallengesCount(dayStart);
            challengeEntries.add(new BarEntry(i, dayChallenges));
        }
        
        BarDataSet quizDataSet = new BarDataSet(quizEntries, "Quizzes");
        quizDataSet.setColor(Color.GREEN);
        
        BarDataSet challengeDataSet = new BarDataSet(challengeEntries, "Retos");
        challengeDataSet.setColor(getResources().getColor(R.color.accent_color));
        
        BarData barData = new BarData(quizDataSet, challengeDataSet);
        barData.setBarWidth(0.4f);
        
        weeklyChart.setData(barData);
        weeklyChart.getDescription().setText("Actividad Semanal");
        weeklyChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        weeklyChart.invalidate();
    }
}
