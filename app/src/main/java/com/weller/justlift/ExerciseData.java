package com.weller.justlift;

public class ExerciseData {

    private String exercise;

    public ExerciseData(String exerciseName){
        exercise = exerciseName;
    }
    public String getExerciseName() {
        return exercise;
    }

    public void setExerciseName(String iexercise) {
         exercise = iexercise;
    }

}
