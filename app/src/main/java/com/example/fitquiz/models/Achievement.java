package com.example.fitquiz.models;

public class Achievement {
    private String title;
    private String description;
    private boolean unlocked;
    private String progress;
    private int iconResource;
    
    public Achievement(String title, String description, boolean unlocked, String progress, int iconResource) {
        this.title = title;
        this.description = description;
        this.unlocked = unlocked;
        this.progress = progress;
        this.iconResource = iconResource;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isUnlocked() {
        return unlocked;
    }
    
    public String getProgress() {
        return progress;
    }
    
    public int getIconResource() {
        return iconResource;
    }
}
