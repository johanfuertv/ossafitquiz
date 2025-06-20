package com.example.fitquiz;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    
    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DURATION = 3000; // 3 seconds
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Cargar idioma guardado
        loadLocale();
        
        Log.d(TAG, "SplashActivity onCreate started");
        
        try {
            setContentView(R.layout.activity_splash);
            Log.d(TAG, "Layout set successfully");
            
            // Simple text view for testing
            TextView titleText = findViewById(R.id.app_title);
            if (titleText != null) {
                titleText.setText(getString(R.string.app_name));
                Log.d(TAG, "Title text set");
            }
            
            new Handler().postDelayed(() -> {
                Log.d(TAG, "Starting MainActivity");
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }, SPLASH_DURATION);
            
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
        }
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
}
