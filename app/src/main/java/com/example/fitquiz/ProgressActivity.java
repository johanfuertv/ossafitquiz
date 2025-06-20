package com.example.fitquiz;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fitquiz.database.AppDatabase;
import com.example.fitquiz.database.entities.QuizResult;
import com.example.fitquiz.database.entities.ChallengeResult;
import com.example.fitquiz.database.entities.UserProgress;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProgressActivity extends AppCompatActivity {
    
    private AppDatabase database;
    private TextView totalQuizzesText;
    private TextView averageScoreText;
    private TextView totalChallengesText;
    private TextView currentStreakText;
    private TextView maxStreakText;
    private LineChart progressChart;
    private BarChart weeklyChart;
    private PieChart activityChart;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        
        database = AppDatabase.getDatabase(this);
        
        initializeViews();
        loadStatistics();
        setupCharts();
    }
    
    private void initializeViews() {
        totalQuizzesText = findViewById(R.id.total_quizzes_text);
        averageScoreText = findViewById(R.id.average_score_text);
        totalChallengesText = findViewById(R.id.total_challenges_text);
        currentStreakText = findViewById(R.id.current_streak_text);
        maxStreakText = findViewById(R.id.max_streak_text);
        progressChart = findViewById(R.id.progress_chart);
        weeklyChart = findViewById(R.id.weekly_chart);
        activityChart = findViewById(R.id.activity_chart);
    }
    
    private void loadStatistics() {
        // Get week start date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        long weekStart = calendar.getTimeInMillis();
        
        // Load quiz statistics
        List<QuizResult> quizResults = database.quizResultDao().getResultsFromDate(weekStart);
        totalQuizzesText.setText(String.valueOf(quizResults.size()));
        
        if (!quizResults.isEmpty()) {
            double averageScore = database.quizResultDao().getAverageScore(weekStart);
            averageScoreText.setText(String.format("%.1f%%", averageScore * 20)); // Convert to percentage
        } else {
            averageScoreText.setText("0%");
        }
        
        // Load challenge statistics
        int completedChallenges = database.challengeResultDao().getCompletedChallengesCount(weekStart);
        totalChallengesText.setText(String.valueOf(completedChallenges));
        
        // Load streak information
        int maxStreak = database.userProgressDao().getMaxStreak();
        maxStreakText.setText(String.valueOf(maxStreak));
        
        // Get current streak
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        
        UserProgress todayProgress = database.userProgressDao().getProgressForDate(today.getTimeInMillis());
        if (todayProgress != null) {
            currentStreakText.setText(String.valueOf(todayProgress.getStreak()));
        } else {
            currentStreakText.setText("0");
        }
    }
    
    private void setupCharts() {
        setupProgressChart();
        setupWeeklyChart();
        setupActivityChart();
    }
    
    private void setupProgressChart() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        
        List<UserProgress> progressList = database.userProgressDao().getProgressFromDate(calendar.getTimeInMillis());
        
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            
            // Find progress for this day
            UserProgress dayProgress = null;
            for (UserProgress progress : progressList) {
                Calendar progressCal = Calendar.getInstance();
                progressCal.setTime(progress.getDate());
                if (progressCal.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
                    dayProgress = progress;
                    break;
                }
            }
            
            int score = dayProgress != null ? dayProgress.getDailyScore() : 0;
            entries.add(new Entry(i, score));
            
            // Add day label
            String[] days = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
            labels.add(days[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        }
        
        LineDataSet dataSet = new LineDataSet(entries, "Puntuación Diaria");
        dataSet.setColor(getColor(R.color.primary_color));
        dataSet.setCircleColor(getColor(R.color.primary_color));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(6f);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(getColor(R.color.text_primary));
        
        LineData lineData = new LineData(dataSet);
        progressChart.setData(lineData);
        progressChart.getDescription().setText("Progreso de la Semana");
        progressChart.getDescription().setTextColor(getColor(R.color.text_primary));
        progressChart.getDescription().setTextSize(14f);
        
        // Customize X axis
        XAxis xAxis = progressChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setTextColor(getColor(R.color.text_secondary));
        
        // Customize Y axes
        YAxis leftAxis = progressChart.getAxisLeft();
        leftAxis.setTextColor(getColor(R.color.text_secondary));
        
        YAxis rightAxis = progressChart.getAxisRight();
        rightAxis.setEnabled(false);
        
        progressChart.invalidate();
    }
    
    private void setupWeeklyChart() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        
        ArrayList<BarEntry> quizEntries = new ArrayList<>();
        ArrayList<BarEntry> challengeEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            long dayStart = calendar.getTimeInMillis();
            
            // Count quizzes for this day
            List<QuizResult> dayQuizzes = database.quizResultDao().getResultsFromDate(dayStart);
            quizEntries.add(new BarEntry(i, dayQuizzes.size()));
            
            // Count challenges for this day
            int dayChallenges = database.challengeResultDao().getCompletedChallengesCount(dayStart);
            challengeEntries.add(new BarEntry(i, dayChallenges));
            
            // Add day label
            String[] days = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
            labels.add(days[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        }
        
        BarDataSet quizDataSet = new BarDataSet(quizEntries, "Quizzes");
        quizDataSet.setColor(getColor(R.color.success_color));
        
        BarDataSet challengeDataSet = new BarDataSet(challengeEntries, "Retos");
        challengeDataSet.setColor(getColor(R.color.accent_color));
        
        BarData barData = new BarData(quizDataSet, challengeDataSet);
        barData.setBarWidth(0.35f);
        
        weeklyChart.setData(barData);
        weeklyChart.getDescription().setText("Actividad Semanal");
        weeklyChart.getDescription().setTextColor(getColor(R.color.text_primary));
        weeklyChart.getDescription().setTextSize(14f);
        
        // Customize X axis
        XAxis xAxis = weeklyChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setTextColor(getColor(R.color.text_secondary));
        
        // Group bars
        weeklyChart.groupBars(0f, 0.3f, 0.05f);
        
        // Customize Y axes
        YAxis leftAxis = weeklyChart.getAxisLeft();
        leftAxis.setTextColor(getColor(R.color.text_secondary));
        leftAxis.setAxisMinimum(0f);
        
        YAxis rightAxis = weeklyChart.getAxisRight();
        rightAxis.setEnabled(false);
        
        weeklyChart.invalidate();
    }
    
    private void setupActivityChart() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        long weekStart = calendar.getTimeInMillis();
        
        // Get activity data
        List<QuizResult> quizResults = database.quizResultDao().getResultsFromDate(weekStart);
        int completedChallenges = database.challengeResultDao().getCompletedChallengesCount(weekStart);
        
        ArrayList<PieEntry> entries = new ArrayList<>();
        
        if (quizResults.size() > 0) {
            entries.add(new PieEntry(quizResults.size(), "Quizzes"));
        }
        
        if (completedChallenges > 0) {
            entries.add(new PieEntry(completedChallenges, "Retos"));
        }
        
        if (entries.isEmpty()) {
            entries.add(new PieEntry(1, "Sin actividad"));
        }
        
        PieDataSet dataSet = new PieDataSet(entries, "");
        
        // Set colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getColor(R.color.success_color));
        colors.add(getColor(R.color.accent_color));
        colors.add(getColor(R.color.text_secondary));
        dataSet.setColors(colors);
        
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.WHITE);
        
        PieData pieData = new PieData(dataSet);
        activityChart.setData(pieData);
        activityChart.getDescription().setText("Distribución de Actividades");
        activityChart.getDescription().setTextColor(getColor(R.color.text_primary));
        activityChart.getDescription().setTextSize(14f);
        activityChart.setHoleRadius(40f);
        activityChart.setTransparentCircleRadius(45f);
        activityChart.invalidate();
    }
}
