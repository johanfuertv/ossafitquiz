package com.example.fitquiz.models;

public class Challenge {
    private String name;
    private String description;
    private int targetReps;
    private String instructions;
    private String imageUrl;
    
    public Challenge(String name, String description, int targetReps, String instructions, String imageUrl) {
        this.name = name;
        this.description = description;
        this.targetReps = targetReps;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getTargetReps() { return targetReps; }
    public String getInstructions() { return instructions; }
    public String getImageUrl() { return imageUrl; }
}
