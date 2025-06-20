package com.example.fitquiz.utils;

import com.example.fitquiz.models.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizDataProvider {
    
    public static List<Question> getDailyQuestions() {
        List<Question> allQuestions = getAllQuestions();
        Collections.shuffle(allQuestions);
        return allQuestions.subList(0, Math.min(5, allQuestions.size()));
    }
    
    private static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        
        questions.add(new Question(
            "¿Cuántos minutos de ejercicio cardiovascular se recomiendan por semana?",
            Arrays.asList("75 minutos", "150 minutos", "300 minutos", "500 minutos"),
            1,
            "La OMS recomienda al menos 150 minutos de actividad aeróbica moderada por semana."
        ));
        
        questions.add(new Question(
            "¿Cuál es el mejor momento para hidratarse durante el ejercicio?",
            Arrays.asList("Solo antes", "Solo después", "Durante todo el ejercicio", "Solo cuando tengas sed"),
            2,
            "Es importante mantenerse hidratado antes, durante y después del ejercicio."
        ));
        
        questions.add(new Question(
            "¿Cuántas horas de sueño se recomiendan para una recuperación óptima?",
            Arrays.asList("5-6 horas", "7-9 horas", "10-12 horas", "4-5 horas"),
            1,
            "Los adultos necesitan entre 7-9 horas de sueño para una recuperación adecuada."
        ));
        
        questions.add(new Question(
            "¿Qué nutriente es más importante después del ejercicio?",
            Arrays.asList("Grasas", "Proteínas", "Carbohidratos", "Vitaminas"),
            1,
            "Las proteínas son esenciales para la reparación y construcción muscular post-ejercicio."
        ));
        
        questions.add(new Question(
            "¿Con qué frecuencia se debe hacer ejercicio de fuerza?",
            Arrays.asList("Todos los días", "2-3 veces por semana", "Una vez por semana", "Solo los fines de semana"),
            1,
            "Se recomienda hacer ejercicios de fuerza 2-3 veces por semana para obtener beneficios óptimos."
        ));
        
        questions.add(new Question(
            "¿Cuál es la duración ideal para un calentamiento?",
            Arrays.asList("2-3 minutos", "5-10 minutos", "15-20 minutos", "30 minutos"),
            1,
            "Un calentamiento de 5-10 minutos es ideal para preparar el cuerpo para el ejercicio."
        ));
        
        questions.add(new Question(
            "¿Qué porcentaje de agua compone el cuerpo humano adulto?",
            Arrays.asList("45%", "60%", "75%", "85%"),
            1,
            "Aproximadamente el 60% del cuerpo humano adulto está compuesto por agua."
        ));
        
        questions.add(new Question(
            "¿Cuál es el mejor tipo de ejercicio para la salud cardiovascular?",
            Arrays.asList("Levantamiento de pesas", "Ejercicio aeróbico", "Yoga", "Estiramientos"),
            1,
            "El ejercicio aeróbico es el más beneficioso para la salud cardiovascular."
        ));
        
        return questions;
    }
}
