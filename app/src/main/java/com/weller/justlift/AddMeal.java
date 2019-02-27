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
        setContentView(R.layout.add_meal_layout);//sets layout to add_meal_layout xml
        getWindow().setLayout(MainActivity.displayWidth, ViewGroup.LayoutParams.WRAP_CONTENT);//sets the layout to display width in main activity
        addMeal = findViewById(R.id.addMealButton);
        mealEditText = findViewById(R.id.text_meal);
        caloriesEditText = findViewById(R.id.text_calories);
        proteinEditText = findViewById(R.id.text_protein);//initialises elements on screen
        db = new ProfileDB(getApplicationContext());//initialises db class

        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//listens to clicks on add button
                mealString = mealEditText.getText().toString();
                caloriesString = caloriesEditText.getText().toString();
                proteinString = proteinEditText.getText().toString();//when add button selected, user entries are stored as strings

                if (mealString.length() != 0) {//if user has entered information
                    dayCount = db.getDayCount(getApplicationContext());//gets the current number of days since user started
                    test = db.addMealData(mealString, caloriesString, proteinString, dayCount);//adds meal data to the meal table
                    if(test)//if successful
                    {
                        toastMessage("successful");
                        setResult(Activity.RESULT_OK);//tells fragment data has been stored and in fragment we override onActivityResult to refresh list
                        finish();
                    }
                    else{//if fails
                        toastMessage("failed");
                    }

                }
                else{//if no data entered, displays error
                    Toast.makeText(getApplicationContext(), "Enter text", Toast.LENGTH_LONG).show();
                }
            }


            });
    }
    private void toastMessage(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }


}
