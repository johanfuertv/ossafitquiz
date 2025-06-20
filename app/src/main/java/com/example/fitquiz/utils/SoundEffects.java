package com.example.fitquiz.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import com.example.fitquiz.R;

public class SoundEffects {
    
    private SoundPool soundPool;
    private int startSound;
    private int successSound;
    private int failSound;
    private int buttonSound;
    private int repSound;
    private int tickSound;
    private int victorySound;
    private int tryAgainSound;
    
    public SoundEffects(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build();
        
        soundPool = new SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build();
        
        // Cargar sonidos
        startSound = soundPool.load(context, R.raw.start_sound, 1);
        successSound = soundPool.load(context, R.raw.success_sound, 1);
        failSound = soundPool.load(context, R.raw.fail_sound, 1);
        buttonSound = soundPool.load(context, R.raw.button_sound, 1);
        repSound = soundPool.load(context, R.raw.rep_sound, 1);
        tickSound = soundPool.load(context, R.raw.tick_sound, 1);
        victorySound = soundPool.load(context, R.raw.victory_sound, 1);
        tryAgainSound = soundPool.load(context, R.raw.try_again_sound, 1);
    }
    
    public void playStartSound() {
        soundPool.play(startSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    
    public void playSuccessSound() {
        soundPool.play(successSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    
    public void playFailSound() {
        soundPool.play(failSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    
    public void playButtonSound() {
        soundPool.play(buttonSound, 0.5f, 0.5f, 1, 0, 1.0f);
    }
    
    public void playRepSound() {
        soundPool.play(repSound, 0.7f, 0.7f, 1, 0, 1.0f);
    }
    
    public void playTickSound() {
        soundPool.play(tickSound, 0.3f, 0.3f, 1, 0, 1.0f);
    }
    
    public void playVictorySound() {
        soundPool.play(victorySound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    
    public void playTryAgainSound() {
        soundPool.play(tryAgainSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    
    public void release() {
        soundPool.release();
        soundPool = null;
    }
}
