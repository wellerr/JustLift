package com.weller.justlift;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class AddExercise extends AppCompatActivity {

    Button addExercise;
    TextInputEditText exerciseEditText;

    boolean test;
    public ProfileDB db;
    public NutritionFragment nutrition;


    String exerciseString;

    private ListView listView;

    int dayCount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise_layout);
        getWindow().setLayout(MainActivity.displayWidth, ViewGroup.LayoutParams.WRAP_CONTENT);//sets params of dialog box
        addExercise = findViewById(R.id.addExerciseButton);
        exerciseEditText = findViewById(R.id.text_exercise);

        db = new ProfileDB(getApplicationContext());

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseString = exerciseEditText.getText().toString();

                if (exerciseString.length() != 0) {
                    dayCount = db.getDayCount(getApplicationContext());//gets the current number of days since user started
                    test = db.addExerciseData(exerciseString);
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
