package com.weller.justlift;

public class NutritionData {

    private String meal;
    private String calories;
    private String protein;

    public NutritionData(String mealInput, String caloriesInput, String proteinInput){
        meal = mealInput;
        calories = caloriesInput;
        protein = proteinInput;
    }
    public String getMealName() {
        return meal;
    }

    public void setMealName(String imeal) {
        meal = imeal;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String icalories) {
        calories = icalories;
    }

    public String getProtein(){
        return protein;
    }

    public void setProtein(String iprotein) {
        protein = iprotein;
    }
}
