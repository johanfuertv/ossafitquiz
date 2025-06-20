package com.example.fitquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private static final String USERNAME = "user";
    private static final String PASSWORD = "123";
    
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button changeLanguageButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Cargar idioma guardado
        loadLocale();
        
        setContentView(R.layout.activity_login);
        
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        changeLanguageButton = findViewById(R.id.change_language_button);
        
        // Configurar botón de login
        loginButton.setOnClickListener(v -> attemptLogin());
        
        // Configurar botón de cambio de idioma
        changeLanguageButton.setOnClickListener(v -> changeLanguage());
        
        // Iniciar servicio de música
        startMusicService();
    }
    
    private void attemptLogin() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (username.equals(USERNAME) && password.equals(PASSWORD)) {
            // Login exitoso
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Login fallido
            Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show();
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
        
        // Reiniciar actividad para aplicar cambios
        recreate();
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
    
    private void startMusicService() {
        Intent musicIntent = new Intent(this, MusicService.class);
        startService(musicIntent);
    }
}
