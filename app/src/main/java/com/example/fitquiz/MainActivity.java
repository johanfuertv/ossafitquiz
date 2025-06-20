package com.example.fitquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.fitquiz.database.AppDatabase;
import com.example.fitquiz.database.entities.UserProgress;
import com.example.fitquiz.fragments.AchievementsFragment;
import com.example.fitquiz.fragments.HomeFragment;
import com.example.fitquiz.fragments.StatsFragment;
import com.example.fitquiz.utils.SoundEffects;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "FitQuiz_MainActivity";
    private AppDatabase database;
    private BottomNavigationView bottomNavigation;
    private boolean musicEnabled = true;
    private SoundEffects soundEffects;
    private FloatingActionButton achievementsButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Cargar idioma guardado
        loadLocale();
        
        Log.d(TAG, "FitQuiz MainActivity iniciando...");
        
        setContentView(R.layout.activity_main);
        
        // Inicializar efectos de sonido
        soundEffects = new SoundEffects(this);
        
        // Inicializar base de datos de FitQuiz
        database = AppDatabase.getDatabase(this);
        
        // Configurar navegación
        bottomNavigation = findViewById(R.id.bottom_navigation);
        achievementsButton = findViewById(R.id.achievements_button);
        
        // Cargar fragment inicial
        loadFragment(new HomeFragment());
        
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                soundEffects.playButtonSound();
            } else if (item.getItemId() == R.id.nav_stats) {
                selectedFragment = new StatsFragment();
                soundEffects.playButtonSound();
            }
            
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            
            return true;
        });
        
        // Configurar botón de logros
        achievementsButton.setOnClickListener(v -> {
            soundEffects.playButtonSound();
            loadFragment(new AchievementsFragment());
        });
        
        // Animar botón de logros
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse_animation);
        achievementsButton.startAnimation(pulse);
        
        // Inicializar progreso diario
        initializeDailyProgress();
        
        // Cargar preferencia de música
        musicEnabled = getSharedPreferences("Settings", MODE_PRIVATE)
                .getBoolean("music_enabled", true);
        
        // Mostrar mensaje de bienvenida
        showWelcomeMessage();
        
        Log.d(TAG, "FitQuiz MainActivity configurado correctamente");
    }
    
    private void showWelcomeMessage() {
        View welcomeOverlay = findViewById(R.id.welcome_overlay);
        TextView welcomeMessage = findViewById(R.id.welcome_message);
        
        // Obtener nombre de usuario
        String username = getSharedPreferences("Settings", MODE_PRIVATE)
                .getString("username", "user");
        
        welcomeMessage.setText(String.format(getString(R.string.welcome_back), username));
        
        welcomeOverlay.setVisibility(View.VISIBLE);
        welcomeOverlay.animate()
            .alpha(0f)
            .setDuration(3000)
            .withEndAction(() -> welcomeOverlay.setVisibility(View.GONE));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        
        MenuItem switchItem = menu.findItem(R.id.action_music);
        Switch musicSwitch = (Switch) switchItem.getActionView();
        musicSwitch.setChecked(musicEnabled);
        
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            musicEnabled = isChecked;
            toggleMusicService(musicEnabled);
            soundEffects.playButtonSound();
        });
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_language) {
            changeLanguage();
            soundEffects.playButtonSound();
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            logout();
            soundEffects.playButtonSound();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        
        // Añadir animaciones de transición
        fragmentTransaction.setCustomAnimations(
            R.anim.slide_in_right, 
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        );
        
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
    
    private void initializeDailyProgress() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        Date today = calendar.getTime();
        UserProgress todayProgress = database.userProgressDao().getProgressForDate(today.getTime());
        
        if (todayProgress == null) {
            UserProgress newProgress = new UserProgress(today, 0, false, false, 0);
            database.userProgressDao().insert(newProgress);
            Log.d(TAG, "Progreso diario inicializado para FitQuiz");
        }
    }
    
    private void changeLanguage() {
        // Alternar entre español e inglés
        String currentLang = getSharedPreferences("Settings", MODE_PRIVATE)
                .getString("language", "es");
        
        String newLang = currentLang.equals("es") ? "en" : "es";
        
        // Guardar preferencia de idioma
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("language", newLang);
        editor.apply();
        
        // Actualizar configuración de idioma
        Locale locale = new Locale(newLang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        
        // Mostrar mensaje de cambio de idioma
        String message = newLang.equals("es") ? "Idioma cambiado a Español" : "Language changed to English";
        showFloatingMessage(message);
        
        // Reiniciar actividad para aplicar cambios
        recreate();
    }
    
    private void showFloatingMessage(String message) {
        View messageView = findViewById(R.id.floating_message);
        if (messageView instanceof TextView) {
            TextView messageText = (TextView) messageView;
            messageText.setText(message);
            messageView.setVisibility(View.VISIBLE);
            
            messageView.animate()
                .translationY(-50f)
                .alpha(0f)
                .setDuration(2000)
                .withEndAction(() -> {
                    messageView.setVisibility(View.GONE);
                    messageView.setTranslationY(0f);
                    messageView.setAlpha(1f);
                });
        }
    }

    private void toggleMusicService(boolean enable) {
        Intent intent = new Intent(this, MusicService.class);
        
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putBoolean("music_enabled", enable);
        editor.apply();
        
        if (enable) {
            startService(intent);
        } else {
            stopService(intent);
        }
    }
    
    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    
    private void loadLocale() {
        String lang = getSharedPreferences("Settings", MODE_PRIVATE)
                .getString("language", "es");
        
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener servicio de música si la actividad se destruye
        Intent musicIntent = new Intent(this, MusicService.class);
        stopService(musicIntent);
        
        if (soundEffects != null) {
            soundEffects.release();
        }
    }
}
