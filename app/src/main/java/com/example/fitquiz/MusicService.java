package com.example.fitquiz;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
    
    private static final String TAG = "MusicService";
    private MediaPlayer mediaPlayer;
    
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(0.5f, 0.5f);
        } catch (Exception e) {
            Log.e(TAG, "Error al crear MediaPlayer: " + e.getMessage());
        }
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            // Verificar si la música está habilitada
            SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
            boolean musicEnabled = prefs.getBoolean("music_enabled", true);
            
            if (musicEnabled && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                Log.d(TAG, "Música iniciada");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al iniciar música: " + e.getMessage());
        }
        
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
                Log.d(TAG, "Música detenida y liberada");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al detener música: " + e.getMessage());
        }
        super.onDestroy();
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
