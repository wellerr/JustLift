package com.weller.justlift;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class AddWeeklyWeight extends AppCompatActivity{
    Button updateWeightButton;
    TextInputEditText updateWeightText;
    public ProfileDB db;
    int dayCount;
    double caloriesLeft;
    String TAG = "AddWeeklyWeight";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_weekly_weight_layout);//sets layout to add_weekly_weight_layout xml
        getWindow().setLayout(MainActivity.displayWidth, ViewGroup.LayoutParams.WRAP_CONTENT);//sets display to display width from main activity
        updateWeightButton = findViewById(R.id.addUserWeight);
        updateWeightText = findViewById(R.id.text_weight);//initialise elements on screen
        db = new ProfileDB(getApplicationContext());//initialise db class
        Intent intent = getIntent();
        caloriesLeft = intent.getDoubleExtra("caloriesLeft", 0);//gets caloriesleft from week

        updateWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//listens for update button press
                if (updateWeightText.getText().toString().isEmpty()){//if nothing entered
                Toast.makeText(getApplicationContext(), "Enter weight", Toast.LENGTH_LONG).show();//show error
            }
            else if(updateWeightText.getText().toString().isEmpty() == false) {//if user enters info
                double updatedWeight = Double.parseDouble(updateWeightText.getText().toString());//converts weight to int
                    Log.i(TAG, Double.toString(updatedWeight));//logs weight for testing
                dayCount++;//when added daycount increments
                db.updateDayCount(getApplicationContext(), dayCount);//updates daycount
                db.addToWeeklyTable(updatedWeight);//adds weight for week and total calories for week
                db.deleteNutritionData(dayCount);//deletes the days content after copying it to past meal table
                db.deletePastMeals();//deletes table data to not clog up user device ready for the next week values
                Intent i = new Intent(AddWeeklyWeight.this, SplashScreen.class);//runs splash screen class
                i.putExtra("Code", 1);//passes code into splash screen that tells
                i.putExtra("Calories", caloriesLeft);
                //splash screen it is performing linear regression algorithm
                startActivity(i);
            }
            }
            /* private void showAddItemDialog(final Context context) {
        final EditText taskEditText = new EditText(context);
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        taskEditText.setHint("Weight (kg)");
        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme_Dialog))//code pulled from https://alvinalexander.com/source-code/android-mockup-prototype-dialog-text-field
                .setTitle("Update your weight")
                .setMessage("Please update your weight before continuing")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (taskEditText.getText().toString().isEmpty()){
                            Toast.makeText(context, "Enter weight", Toast.LENGTH_LONG).show();
                        }
                        else if(taskEditText.getText().toString().isEmpty() == false) {
                            int updatedWeight = Integer.parseInt(taskEditText.getText().toString());
                            dayCount++;//when added daycount increments
                            db.updateDayCount(getContext(), dayCount);//updates daycount
                            db.addToWeeklyTable(updatedWeight);//adds weight to the weekly table
                            db.deleteNutritionData(dayCount);
                            db.deletePastMeals();//deletes table data to not clog up user device
                           // reloadFragment();//reloads fragment so listview updated
                            Intent i = new Intent(mActivity, SplashScreen.class);
                            i.putExtra("Code", 1);//passes code into splash screen that tells
                            i.putExtra("Calories", caloriesLeft);
                            //splash screen it is performing linear regression algorithm
                           mActivity.startActivity(i);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
    */
        });
    }
}

