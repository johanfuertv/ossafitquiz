package com.example.fitquiz;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.airbnb.lottie.LottieAnimationView;
import com.example.fitquiz.database.AppDatabase;
import com.example.fitquiz.database.entities.QuizResult;
import com.example.fitquiz.database.entities.UserProgress;
import com.example.fitquiz.models.Question;
import com.example.fitquiz.utils.QuizDataProvider;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    
    private AppDatabase database;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private long startTime;
    
    private TextView questionText;
    private Button[] optionButtons;
    private TextView questionCounter;
    private ProgressBar progressBar;
    private LottieAnimationView resultAnimation;
    private CardView resultCard;
    private TextView resultText;
    private Button nextButton;
    private TextView explanationText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        
        database = AppDatabase.getDatabase(this);
        questions = QuizDataProvider.getDailyQuestions();
        startTime = System.currentTimeMillis();
        
        initializeViews();
        displayQuestion();
    }
    
    private void initializeViews() {
        questionText = findViewById(R.id.question_text);
        questionCounter = findViewById(R.id.question_counter);
        progressBar = findViewById(R.id.progress_bar);
        resultAnimation = findViewById(R.id.result_animation);
        resultCard = findViewById(R.id.result_card);
        resultText = findViewById(R.id.result_text);
        nextButton = findViewById(R.id.next_button);
        explanationText = findViewById(R.id.explanation_text);
        
        optionButtons = new Button[4];
        optionButtons[0] = findViewById(R.id.option_1);
        optionButtons[1] = findViewById(R.id.option_2);
        optionButtons[2] = findViewById(R.id.option_3);
        optionButtons[3] = findViewById(R.id.option_4);
        
        for (int i = 0; i < optionButtons.length; i++) {
            final int index = i;
            optionButtons[i].setOnClickListener(v -> selectAnswer(index));
        }
        
        nextButton.setOnClickListener(v -> nextQuestion());
        
        progressBar.setMax(questions.size());
    }
    
    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            
            questionText.setText(question.getQuestion());
            questionCounter.setText(String.format("Pregunta %d de %d", 
                currentQuestionIndex + 1, questions.size()));
            
            List<String> options = question.getOptions();
            for (int i = 0; i < optionButtons.length; i++) {
                if (i < options.size()) {
                    optionButtons[i].setText(options.get(i));
                    optionButtons[i].setVisibility(View.VISIBLE);
                    optionButtons[i].setEnabled(true);
                    optionButtons[i].setBackgroundTintList(getColorStateList(R.color.primary_color));
                } else {
                    optionButtons[i].setVisibility(View.GONE);
                }
            }
            
            progressBar.setProgress(currentQuestionIndex + 1);
            resultCard.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
        } else {
            finishQuiz();
        }
    }
    
    private void selectAnswer(int selectedIndex) {
        Question question = questions.get(currentQuestionIndex);
        boolean isCorrect = selectedIndex == question.getCorrectAnswer();
        
        if (isCorrect) {
            score++;
            optionButtons[selectedIndex].setBackgroundTintList(getColorStateList(R.color.success_color));
            showResultAnimation(true);
        } else {
            optionButtons[selectedIndex].setBackgroundTintList(getColorStateList(R.color.error_color));
            optionButtons[question.getCorrectAnswer()].setBackgroundTintList(getColorStateList(R.color.success_color));
            showResultAnimation(false);
        }
        
        // Disable all buttons
        for (Button button : optionButtons) {
            button.setEnabled(false);
        }
        
        // Show explanation
        explanationText.setText(question.getExplanation());
        explanationText.setVisibility(View.VISIBLE);
        
        // Show next button
        nextButton.setVisibility(View.VISIBLE);
    }
    
    private void showResultAnimation(boolean isCorrect) {
        resultCard.setVisibility(View.VISIBLE);
        
        if (isCorrect) {
            resultAnimation.setAnimation("success_animation.json");
            resultText.setText("¡Correcto!");
            resultText.setTextColor(getColor(R.color.success_color));
        } else {
            resultAnimation.setAnimation("error_animation.json");
            resultText.setText("Incorrecto");
            resultText.setTextColor(getColor(R.color.error_color));
        }
        
        resultAnimation.playAnimation();
        
        // Animate card appearance
        ObjectAnimator.ofFloat(resultCard, "alpha", 0f, 1f).setDuration(300).start();
    }
    
    private void nextQuestion() {
        currentQuestionIndex++;
        explanationText.setVisibility(View.GONE);
        displayQuestion();
    }
    
    private void finishQuiz() {
        long endTime = System.currentTimeMillis();
        long timeSpent = endTime - startTime;
        
        // Save quiz result
        QuizResult result = new QuizResult(score, questions.size(), new Date(), timeSpent);
        database.quizResultDao().insert(result);
        
        // Update daily progress
        updateDailyProgress();
        
        // Show final result
        showFinalResult();
    }
    
    private void updateDailyProgress() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        Date today = calendar.getTime();
        UserProgress progress = database.userProgressDao().getProgressForDate(today.getTime());
        
        if (progress != null) {
            progress.quizCompleted = true;
            progress.dailyScore += score;
            database.userProgressDao().update(progress);
        }
    }
    
    private void showFinalResult() {
        setContentView(R.layout.activity_quiz_result);
        
        TextView finalScoreText = findViewById(R.id.final_score_text);
        TextView percentageText = findViewById(R.id.percentage_text);
        LottieAnimationView finalAnimation = findViewById(R.id.final_animation);
        Button finishButton = findViewById(R.id.finish_button);
        
        int percentage = (score * 100) / questions.size();
        
        finalScoreText.setText(String.format("%d/%d", score, questions.size()));
        percentageText.setText(String.format("%d%% Correcto", percentage));
        
        if (percentage >= 80) {
            finalAnimation.setAnimation("celebration_animation.json");
        } else if (percentage >= 60) {
            finalAnimation.setAnimation("good_job_animation.json");
        } else {
            finalAnimation.setAnimation("try_again_animation.json");
        }
        
        finalAnimation.playAnimation();
        
        finishButton.setOnClickListener(v -> finish());
    }
}
