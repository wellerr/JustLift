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
        setContentView(R.layout.add_weekly_weight_layout);
        getWindow().setLayout(MainActivity.displayWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        updateWeightButton = findViewById(R.id.addUserWeight);
        updateWeightText = findViewById(R.id.text_weight);
        db = new ProfileDB(getApplicationContext());
        Intent intent = getIntent();
        caloriesLeft = intent.getIntExtra("caloriesLeft", 0);


        updateWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateWeightText.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "Enter weight", Toast.LENGTH_LONG).show();
            }
            else if(updateWeightText.getText().toString().isEmpty() == false) {
                int updatedWeight = Integer.parseInt(updateWeightText.getText().toString());
                    Log.i(TAG, Integer.toString(updatedWeight));
                dayCount++;//when added daycount increments
                db.updateDayCount(getApplicationContext(), dayCount);//updates daycount
                db.addToWeeklyTable(updatedWeight);//adds weight to the weekly table
                db.deleteNutritionData(dayCount);
                db.deletePastMeals();//deletes table data to not clog up user device
                Intent i = new Intent(AddWeeklyWeight.this, SplashScreen.class);
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

