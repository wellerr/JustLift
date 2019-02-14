package com.weller.justlift;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class AddMeal extends AppCompatActivity {

    Button addMeal;
    TextInputEditText mealEditText;
    TextInputEditText caloriesEditText;
    TextInputEditText proteinEditText;

    boolean test;
    public ProfileDB db;
    public NutritionFragment nutrition;


    String mealString;
    String caloriesString;
    String proteinString;
    private ListView listView;

    int dayCount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_meal_layout);
        getWindow().setLayout(MainActivity.displayWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        addMeal = findViewById(R.id.addMealButton);
        mealEditText = findViewById(R.id.text_meal);
        caloriesEditText = findViewById(R.id.text_calories);
        proteinEditText = findViewById(R.id.text_protein);
        db = new ProfileDB(getApplicationContext());

        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealString = mealEditText.getText().toString();
                caloriesString = caloriesEditText.getText().toString();
                proteinString = proteinEditText.getText().toString();

                if (mealString.length() != 0) {
                    dayCount = db.getDayCount(getApplicationContext());//gets the current number of days since user started
                    test = db.addMealData(mealString, caloriesString, proteinString, dayCount);
                    if(test)
                    {
                        toastMessage("successful");
                        setResult(Activity.RESULT_OK);//tells fragment data has been stored and in fragment we override onActivityResult to refresh list
                        finish();
                    }
                    else{
                        toastMessage("failed");
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter text", Toast.LENGTH_LONG).show();
                }
            }


            });
    }
    private void toastMessage(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }


}
