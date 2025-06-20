package com.example.fitquiz.utils;

import com.example.fitquiz.models.Challenge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChallengeDataProvider {
    
    public static Challenge getDailyChallenge() {
        List<Challenge> allChallenges = getAllChallenges();
        Random random = new Random();
        return allChallenges.get(random.nextInt(allChallenges.size()));
    }
    
    private static List<Challenge> getAllChallenges() {
        List<Challenge> challenges = new ArrayList<>();
        
        challenges.add(new Challenge(
            "Sentadillas",
            "Fortalece piernas y glúteos",
            15,
            "1. Párate con los pies separados al ancho de los hombros\n2. Baja como si fueras a sentarte\n3. Mantén la espalda recta\n4. Sube lentamente",
            "squats"
        ));
        
        challenges.add(new Challenge(
            "Flexiones",
            "Fortalece pecho, brazos y core",
            10,
            "1. Posición de plancha con brazos extendidos\n2. Baja el cuerpo hasta casi tocar el suelo\n3. Empuja hacia arriba\n4. Mantén el cuerpo recto",
            "pushups"
        ));
        
        challenges.add(new Challenge(
            "Plancha",
            "Fortalece el core y mejora la estabilidad",
            30, // segundos
            "1. Posición de flexión pero apoyado en antebrazos\n2. Mantén el cuerpo recto\n3. Contrae el abdomen\n4. Respira normalmente",
            "plank"
        ));
        
        challenges.add(new Challenge(
            "Saltos de tijera",
            "Ejercicio cardiovascular completo",
            20,
            "1. Párate con pies juntos y brazos a los lados\n2. Salta separando piernas y levantando brazos\n3. Regresa a la posición inicial\n4. Mantén un ritmo constante",
            "jumping_jacks"
        ));
        
        challenges.add(new Challenge(
            "Burpees",
            "Ejercicio de cuerpo completo",
            8,
            "1. Desde posición de pie, baja a cuclillas\n2. Coloca las manos en el suelo\n3. Salta hacia atrás a posición de plancha\n4. Haz una flexión\n5. Salta hacia adelante y arriba",
            "burpees"
        ));
        
        challenges.add(new Challenge(
            "Abdominales",
            "Fortalece los músculos abdominales",
            15,
            "1. Acuéstate boca arriba con rodillas dobladas\n2. Coloca las manos detrás de la cabeza\n3. Levanta el torso hacia las rodillas\n4. Baja lentamente",
            "crunches"
        ));
        
        return challenges;
    }
}
