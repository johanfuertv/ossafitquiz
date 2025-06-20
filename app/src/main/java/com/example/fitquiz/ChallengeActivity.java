package com.example.fitquiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.example.fitquiz.database.AppDatabase;
import com.example.fitquiz.database.entities.ChallengeResult;
import com.example.fitquiz.database.entities.UserProgress;
import com.example.fitquiz.models.Challenge;
import com.example.fitquiz.utils.ChallengeDataProvider;
import com.example.fitquiz.utils.SoundEffects;
import java.util.Calendar;
import java.util.Date;

public class ChallengeActivity extends AppCompatActivity {
    
    private static final String TAG = "ChallengeActivity";
    private AppDatabase database;
    private Challenge currentChallenge;
    private int completedReps = 0;
    private CountDownTimer timer;
    private boolean isTimerChallenge = false;
    private SoundEffects soundEffects;
    
    private TextView challengeTitle;
    private TextView challengeDescription;
    private ViewFlipper exerciseImageFlipper;
    private TextView instructionsText;
    private TextView targetText;
    private TextView completedText;
    private ProgressBar challengeProgress;
    private Button startButton;
    private Button completeRepButton;
    private Button finishButton;
    private Button watchVideoButton;
    private LottieAnimationView motivationAnimation;
    private TextView timerText;
    private TextView pointsText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            Log.d(TAG, "Iniciando ChallengeActivity");
            setContentView(R.layout.activity_challenge);
            
            database = AppDatabase.getDatabase(this);
            currentChallenge = ChallengeDataProvider.getDailyChallenge();
            
            // Verificar que el reto no sea null
            if (currentChallenge == null) {
                Log.e(TAG, "currentChallenge es null");
                showErrorAndFinish("No se pudo cargar el reto diario");
                return;
            }
            
            soundEffects = new SoundEffects(this);
            
            initializeViews();
            setupChallenge();
            
        } catch (Exception e) {
            Log.e(TAG, "Error en onCreate: " + e.getMessage(), e);
            showErrorAndFinish("Error al inicializar: " + e.getMessage());
        }
    }
    
    private void showErrorAndFinish(String errorMessage) {
        setContentView(R.layout.activity_main_error);
        TextView errorText = findViewById(R.id.error_text);
        if (errorText != null) {
            errorText.setText(getString(R.string.challenge_error) + errorMessage);
        }
        
        // Botón para volver
        Button backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }
    }
    
    private void initializeViews() {
        try {
            challengeTitle = findViewById(R.id.challenge_title);
            challengeDescription = findViewById(R.id.challenge_description);
            exerciseImageFlipper = findViewById(R.id.exercise_image_flipper);
            instructionsText = findViewById(R.id.instructions_text);
            targetText = findViewById(R.id.target_text);
            completedText = findViewById(R.id.completed_text);
            challengeProgress = findViewById(R.id.challenge_progress);
            startButton = findViewById(R.id.start_button);
            completeRepButton = findViewById(R.id.complete_rep_button);
            finishButton = findViewById(R.id.finish_button);
            watchVideoButton = findViewById(R.id.watch_video_button);
            motivationAnimation = findViewById(R.id.motivation_animation);
            timerText = findViewById(R.id.timer_text);
            pointsText = findViewById(R.id.points_text);
            
            // Verificar que todas las vistas existan
            if (challengeTitle == null || challengeDescription == null || 
                exerciseImageFlipper == null || instructionsText == null ||
                targetText == null || completedText == null ||
                challengeProgress == null || startButton == null ||
                completeRepButton == null || finishButton == null ||
                pointsText == null) {
                throw new RuntimeException("Una o más vistas no se encontraron en el layout");
            }
            
            startButton.setOnClickListener(v -> startChallenge());
            completeRepButton.setOnClickListener(v -> completeRep());
            finishButton.setOnClickListener(v -> finishChallenge());
            
            if (watchVideoButton != null) {
                watchVideoButton.setOnClickListener(v -> watchTutorialVideo());
            }
            
            // Configurar el ViewFlipper para cambiar automáticamente
            exerciseImageFlipper.setAutoStart(true);
            exerciseImageFlipper.setFlipInterval(2000); // 2 segundos por imagen
            
            Log.d(TAG, "Vistas inicializadas correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar vistas: " + e.getMessage(), e);
            throw e;
        }
    }
    
    private void setupChallenge() {
        try {
            // Usar strings simples para evitar errores de formato
            challengeTitle.setText(currentChallenge.getName() != null ? currentChallenge.getName() : "Reto Diario");
            challengeDescription.setText(currentChallenge.getDescription() != null ? currentChallenge.getDescription() : "Descripción del reto");
            instructionsText.setText(currentChallenge.getInstructions() != null ? currentChallenge.getInstructions() : "Sigue las instrucciones");
            
            // Check if it's a timer-based challenge (like plank)
            isTimerChallenge = currentChallenge.getName() != null && 
                              currentChallenge.getName().toLowerCase().contains("plancha");
            
            int targetReps = currentChallenge.getTargetReps();
            
            if (isTimerChallenge) {
                targetText.setText("Objetivo: " + targetReps + " segundos");
                completeRepButton.setText(getString(R.string.hold_position));
            } else {
                targetText.setText("Objetivo: " + targetReps + " repeticiones");
                completeRepButton.setText(getString(R.string.complete_rep));
            }
            
            updateProgress();
            
            // Cargar imágenes secuenciales para el ejercicio
            String imageUrl = currentChallenge.getImageUrl();
            if (imageUrl != null) {
                loadExerciseImages(imageUrl);
            } else {
                addImageToFlipper(R.drawable.ic_fitness_placeholder);
            }
            
            // Hide challenge controls initially
            completeRepButton.setVisibility(View.GONE);
            finishButton.setVisibility(View.GONE);
            if (timerText != null) {
                timerText.setVisibility(View.GONE);
            }
            
            // Mostrar puntos potenciales
            int totalPotentialPoints = targetReps * 2;
            pointsText.setText("Puntos potenciales: " + totalPotentialPoints + " puntos");
            
            Log.d(TAG, "Reto configurado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error al configurar reto: " + e.getMessage(), e);
            throw e;
        }
    }
    
    private void loadExerciseImages(String exerciseType) {
        // Limpiar el ViewFlipper
        exerciseImageFlipper.removeAllViews();
        
        // Cargar imágenes secuenciales según el tipo de ejercicio
        switch (exerciseType) {
            case "squats":
                addImageToFlipper(R.drawable.squat_1);
                addImageToFlipper(R.drawable.squat_2);
                addImageToFlipper(R.drawable.squat_3);
                break;
            case "pushups":
                addImageToFlipper(R.drawable.pushup_1);
                addImageToFlipper(R.drawable.pushup_2);
                addImageToFlipper(R.drawable.pushup_3);
                break;
            case "plank":
                addImageToFlipper(R.drawable.plank_1);
                addImageToFlipper(R.drawable.plank_2);
                break;
            case "jumping_jacks":
                addImageToFlipper(R.drawable.jumping_jack_1);
                addImageToFlipper(R.drawable.jumping_jack_2);
                addImageToFlipper(R.drawable.jumping_jack_3);
                break;
            case "burpees":
                addImageToFlipper(R.drawable.burpee_1);
                addImageToFlipper(R.drawable.burpee_2);
                addImageToFlipper(R.drawable.burpee_3);
                addImageToFlipper(R.drawable.burpee_4);
                break;
            case "crunches":
                addImageToFlipper(R.drawable.crunch_1);
                addImageToFlipper(R.drawable.crunch_2);
                addImageToFlipper(R.drawable.crunch_3);
                break;
            default:
                addImageToFlipper(R.drawable.ic_fitness_placeholder);
                break;
        }
    }
    
    private void addImageToFlipper(int drawableId) {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(drawableId);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);
        exerciseImageFlipper.addView(imageView);
    }
    
    private void watchTutorialVideo() {
        String videoUrl = getVideoUrlForExercise(currentChallenge.getImageUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(intent);
    }
    
    private String getVideoUrlForExercise(String exerciseType) {
        if (exerciseType == null) return "https://www.youtube.com/results?search_query=fitness+exercises";
        
        switch (exerciseType) {
            case "squats":
                return "https://www.youtube.com/watch?v=YaXPRqUwItQ";
            case "pushups":
                return "https://www.youtube.com/watch?v=IODxDxX7oi4";
            case "plank":
                return "https://www.youtube.com/watch?v=pSHjTRCQxIw";
            case "jumping_jacks":
                return "https://www.youtube.com/shorts/pcUmXXjZy2w";
            case "burpees":
                return "https://www.youtube.com/watch?v=TU8QYVW0gDU";
            case "crunches":
                return "https://www.youtube.com/watch?v=4hmQA3snTyk";
            default:
                return "https://www.youtube.com/results?search_query=fitness+exercises";
        }
    }
    
    private void startChallenge() {
        startButton.setVisibility(View.GONE);
        completeRepButton.setVisibility(View.VISIBLE);
        finishButton.setVisibility(View.VISIBLE);
        
        // Reproducir sonido de inicio
        if (soundEffects != null) {
            soundEffects.playStartSound();
        }
        
        // Usar animación Lottie
        if (motivationAnimation != null) {
            motivationAnimation.setAnimation(R.raw.exercise_animation);
            motivationAnimation.playAnimation();
        }
        
        if (isTimerChallenge) {
            startTimer();
        }
    }
    
    private void startTimer() {
        if (timerText != null) {
            timerText.setVisibility(View.VISIBLE);
        }
        
        timer = new CountDownTimer(currentChallenge.getTargetReps() * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                if (timerText != null) {
                    timerText.setText("Tiempo restante: " + secondsRemaining + " segundos");
                }
                
                // Reproducir sonido de tick cada 5 segundos
                if (secondsRemaining % 5 == 0 && soundEffects != null) {
                    soundEffects.playTickSound();
                }
                
                // Update progress for timer challenges
                int elapsed = currentChallenge.getTargetReps() - secondsRemaining;
                completedReps = elapsed;
                updateProgress();
            }
            
            @Override
            public void onFinish() {
                completedReps = currentChallenge.getTargetReps();
                updateProgress();
                if (timerText != null) {
                    timerText.setText(getString(R.string.time_completed));
                }
                completeRepButton.setEnabled(false);
                showCompletionAnimation();
                if (soundEffects != null) {
                    soundEffects.playSuccessSound();
                }
            }
        }.start();
    }
    
    private void completeRep() {
        if (!isTimerChallenge) {
            completedReps++;
            updateProgress();
            
            // Reproducir sonido de repetición
            if (soundEffects != null) {
                soundEffects.playRepSound();
            }
            
            // Animación de puntos
            showPointsAnimation();
            
            if (completedReps >= currentChallenge.getTargetReps()) {
                completeRepButton.setEnabled(false);
                showCompletionAnimation();
                if (soundEffects != null) {
                    soundEffects.playSuccessSound();
                }
            }
        }
    }
    
    private void showPointsAnimation() {
        TextView pointsAnimation = findViewById(R.id.points_animation);
        if (pointsAnimation != null) {
            pointsAnimation.setText("+2");
            pointsAnimation.setVisibility(View.VISIBLE);
            
            pointsAnimation.animate()
                .translationY(-100f)
                .alpha(0f)
                .setDuration(1000)
                .withEndAction(() -> {
                    pointsAnimation.setVisibility(View.INVISIBLE);
                    pointsAnimation.setTranslationY(0f);
                    pointsAnimation.setAlpha(1f);
                });
        }
    }
    
    private void updateProgress() {
        completedText.setText("Completado: " + completedReps);
        challengeProgress.setMax(currentChallenge.getTargetReps());
        challengeProgress.setProgress(completedReps);
        
        // Actualizar texto de puntos
        int earnedPoints = completedReps * 2;
        int totalPoints = currentChallenge.getTargetReps() * 2;
        pointsText.setText("Puntos ganados: " + earnedPoints + " de " + totalPoints);
        
        // Cambiar color del progreso según el avance
        int percentage = (completedReps * 100) / currentChallenge.getTargetReps();
        if (percentage >= 75) {
            challengeProgress.setProgressTintList(getColorStateList(R.color.success_color));
        } else if (percentage >= 50) {
            challengeProgress.setProgressTintList(getColorStateList(R.color.warning_color));
        }
    }
    
    private void showCompletionAnimation() {
        if (motivationAnimation != null) {
            motivationAnimation.setAnimation(R.raw.celebration_animation);
            motivationAnimation.playAnimation();
        }
    }
    
    private void finishChallenge() {
        if (timer != null) {
            timer.cancel();
        }
        
        boolean completed = completedReps >= currentChallenge.getTargetReps();
        
        // Save challenge result
        ChallengeResult result = new ChallengeResult(
            currentChallenge.getName(),
            currentChallenge.getTargetReps(),
            completedReps,
            completed,
            new Date()
        );
        database.challengeResultDao().insert(result);
        
        // Update daily progress
        updateDailyProgress(completed);
        
        // Show result
        showResult(completed);
    }
    
    private void updateDailyProgress(boolean completed) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        Date today = calendar.getTime();
        UserProgress progress = database.userProgressDao().getProgressForDate(today.getTime());
        
        if (progress != null) {
            progress.challengeCompleted = completed;
            if (completed) {
                // Puntos por completar el reto
                int points = currentChallenge.getTargetReps() * 2;
                progress.dailyScore += points;
                
                // Bonus por completar el reto
                if (completedReps > currentChallenge.getTargetReps()) {
                    progress.dailyScore += 5; // Bonus por superar el objetivo
                }
            } else {
                // Puntos parciales
                progress.dailyScore += completedReps;
            }
            database.userProgressDao().update(progress);
        }
    }
    
    private void showResult(boolean completed) {
        setContentView(R.layout.activity_challenge_result);
        
        TextView resultTitle = findViewById(R.id.result_title);
        TextView resultMessage = findViewById(R.id.result_message);
        TextView pointsEarned = findViewById(R.id.points_earned);
        LottieAnimationView resultAnimation = findViewById(R.id.result_animation);
        Button doneButton = findViewById(R.id.done_button);
        Button shareButton = findViewById(R.id.share_button);
        
        int earnedPoints = completedReps * 2;
        if (completed && completedReps > currentChallenge.getTargetReps()) {
            earnedPoints += 5; // Bonus por superar el objetivo
        }
        final int finalEarnedPoints = earnedPoints;
        
        if (completed) {
            if (resultTitle != null) resultTitle.setText(getString(R.string.challenge_completed));
            if (resultMessage != null) {
                resultMessage.setText("¡Excelente! Completaste " + currentChallenge.getTargetReps() + 
                    (isTimerChallenge ? " segundos" : " repeticiones"));
            }
            if (resultAnimation != null) {
                resultAnimation.setAnimation(R.raw.celebration_animation);
            }
            if (soundEffects != null) {
                soundEffects.playVictorySound();
            }
        } else {
            if (resultTitle != null) resultTitle.setText(getString(R.string.good_try));
            if (resultMessage != null) {
                resultMessage.setText("Completaste " + completedReps + " de " + 
                    currentChallenge.getTargetReps() + ". ¡Sigue así!");
            }
            if (resultAnimation != null) {
                resultAnimation.setAnimation(R.raw.try_again_animation);
            }
            if (soundEffects != null) {
                soundEffects.playTryAgainSound();
            }
        }
        
        if (pointsEarned != null) {
            pointsEarned.setText("¡Ganaste " + earnedPoints + " puntos!");
        }
        
        if (resultAnimation != null) {
            resultAnimation.playAnimation();
        }
        
        if (doneButton != null) {
            doneButton.setOnClickListener(v -> {
                if (soundEffects != null) {
                    soundEffects.playButtonSound();
                }
                finish();
            });
        }
        
        if (shareButton != null) {
            shareButton.setOnClickListener(v -> {
                if (soundEffects != null) {
                    soundEffects.playButtonSound();
                }
                shareResult(completed, finalEarnedPoints);
            });
        }
    }
    
    private void shareResult(boolean completed, int points) {
        String message;
        if (completed) {
            message = "¡He completado el reto de " + currentChallenge.getName() + 
                     " en FitQuiz y he ganado " + points + " puntos! 💪";
        } else {
            message = "He completado " + completedReps + " de " + currentChallenge.getTargetReps() + 
                     " en el reto de " + currentChallenge.getName() + " en FitQuiz y he ganado " + 
                     points + " puntos. ¡Seguiré intentándolo! 💪";
        }
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (soundEffects != null) {
            soundEffects.release();
        }
    }
}
