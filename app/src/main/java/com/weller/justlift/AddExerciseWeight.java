package com.weller.justlift;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddExerciseWeight extends AppCompatActivity {

    Button addWeight;
    TextInputEditText exerciseWeightEditText;
    TextView exerciseName;

    boolean test;
    public ProfileDB db;
    public NutritionFragment nutrition;

    String exerciseWeight;

    private ListView listView;

    int dayCount;
    String TAG = "AddExerciseWeight";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_workout_record_layout);//sets layout to add_workout_record_layout
        getWindow().setLayout(MainActivity.displayWidth, ViewGroup.LayoutParams.WRAP_CONTENT);//sets display size to width in main activity
        addWeight = findViewById(R.id.recordWeightButton);
        exerciseName = findViewById(R.id.tableName);
        exerciseWeightEditText = findViewById(R.id.text_exercise);//initialises the elements on screen
        final int posValue = getIntent().getIntExtra("pos", 0); //gets position of the listview starting from 0
        toastMessage(Integer.toString(posValue));//previews the list item selected
        db = new ProfileDB(getApplicationContext());
        ArrayList<String> names = new ArrayList<>();
        names = exerciseName(names);
        exerciseName.setText(names.get(posValue));
        addWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseWeight = exerciseWeightEditText.getText().toString();

                if (exerciseWeight.length() != 0) {
                    dayCount = db.getDayCount(getApplicationContext());//gets the current number of days since user started
                    String random = "test";

                    switch (posValue){//if position of listvalue, enter into specific table
                        case 0:
                            test = db.addExerciseWeight(db.ExerciseTable_1, Integer.parseInt(exerciseWeight), dayCount);
                            break;
                        case 1:
                            test = db.addExerciseWeight(db.ExerciseTable_2, Integer.parseInt(exerciseWeight), dayCount);
                            break;
                        case 2:
                            test = db.addExerciseWeight(db.ExerciseTable_3, Integer.parseInt(exerciseWeight), dayCount);
                            break;
                        case 3:
                            test = db.addExerciseWeight(db.ExerciseTable_4, Integer.parseInt(exerciseWeight), dayCount);
                            break;
                        case 4:
                            test = db.addExerciseWeight(db.ExerciseTable_5, Integer.parseInt(exerciseWeight), dayCount);
                            break;
                    }
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

    private ArrayList exerciseName(ArrayList names){
        Cursor data = db.getTable(ProfileDB.Table_4);
        int col = data.getColumnCount();//gets number of columns
        while (data.moveToNext()){
            for(int i=1; i<col; i++) {//iterates through columns retrieving user data
                names.add(data.getString(i));
            }
        }
        return names;
    }

    private void toastMessage(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }


}
