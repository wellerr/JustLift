package com.weller.justlift;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.weller.justlift.ProfileDB.TAG;

public class NutritionFragment extends Fragment {
    NutritionData nutrition;
    Activity Load;
    public ProfileDB db;

    Button addMeal;
    Button completeDay;

    private ListView listView;
    private ArrayList<String> caloriesList;
    private ArrayList<String> proteinList;

    public int dayCount;

    TextView setCalories;
    TextView setProtein;
    TextView setRemainingCalories;
    TextView setRemainingProtein;

    Activity mActivity;

    ArrayList<NutritionData> listData = new ArrayList<>();
    Double caloriesLeft;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String ShareName = "CaloriesData";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nutrition, container, false);
        addMeal = v.findViewById(R.id.addMealButton);
        completeDay = v.findViewById(R.id.completeButton);
        db = new ProfileDB(getContext());

        mActivity = getActivity();

        listView = v.findViewById(R.id.listView);
        setCalories = v.findViewById(R.id.setCalories);
        setProtein = v.findViewById(R.id.setProtein);
        setRemainingCalories = v.findViewById(R.id.setRemainingCalories);
        setRemainingProtein = v.findViewById(R.id.setRemainingProtein);
        populateListView();

        prefs = getContext().getSharedPreferences(ShareName,Context.MODE_PRIVATE);
        editor = prefs.edit();

        if(mActivity.getIntent().hasExtra("UpdatedCalories")){/*
          If LinearRegression has happened, updated calories used instead of caluclated BMR
        */
            editor.putFloat("Calories", Float.valueOf(mActivity.getIntent().getStringExtra("UpdatedCalories")));
            editor.commit();//Adds the calories extra passed from linear regression class to the shared prefs of the app
        }
        double totalCalories = totalCalories();
        setCalories.setText(String.valueOf((int)totalCalories));//sets textview to the total calculated
        double totalProtein = totalProtein();
        setProtein.setText(String.valueOf((int)totalProtein)+ "g");
        caloriesLeft = profileData(totalCalories, totalProtein);


        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), com.weller.justlift.AddMeal.class);
                startActivityForResult(intent, 10001);
            }
        });

        completeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               completeDayFunction();
                reloadFragment();
            }
        });

        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent Data){//once add meal has finished...
        super.onActivityResult(requestCode, resultCode, Data);
        if((requestCode == 10001) && (resultCode == Activity.RESULT_OK)){//if request id's match and result ok returned
           reloadFragment();
        }

    }

    public void completeDayFunction(){ //code that completes day of nutrition for the user
        Log.i("complete day", "Daycount is " + dayCount);
        if((dayCount % 7 != 0) ||  (dayCount==0)){//if been 7 days ask user to update their weight
            dayCount = db.getDayCount(getContext());
            dayCount++;//increments days
            db.updateDayCount(getContext(), dayCount);//updates shared prefs with new amount of days
            db.deleteNutritionData(dayCount);//deletes nutrition data, adds nutrition data to the past meals table with the day count as a column
        }
        else{
            showAddItemDialog(getContext());//if end of week ask for user to enter weight

            //then sum the past meals table for calories and put into weekly
            //then clear past meals table
        }
    }
    private void toastMessage(String message){
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }

    public void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the List View.");

        Cursor data = db.getNutritionData();
        listData = new ArrayList<>();

        int col = data.getColumnCount();
        String column = Integer.toString(col);

        int i=0;
        while (data.moveToNext()) {//cursor moves through db
            nutrition = new NutritionData(data.getString(1), data.getString(2), data.getString(3));//gets string variable at each column
            String dataAdded = "Data added " +  data.getString(1) + " " + data.getString(2) + " " + data.getString(3);
            Log.d(TAG, dataAdded);
            listData.add(i,nutrition);//adds data in logical order
            Log.d(TAG,"List adapter starting...");
            NutritionAdapter adapter = new NutritionAdapter(getContext(), R.layout.adapter_view, listData);//puts 3 variables into adapter
            listView.setAdapter(adapter);//views the new table (on screen this shows up as a new row)
            i++;//increments i so next row is done next
        }
    }
    public int totalCalories(){
        Cursor data = db.getCaloriesData();
        int totalCalories =0;//sets initial total to 0
        while (data.moveToNext()){//iterates through calories data
            totalCalories += data.getInt(0);//gets column 0 (first column) moves to next record and adds to total
        }
        Log.d(TAG, "total calories is ..." + totalCalories);
      //  setCalories.setText(Integer.toString(totalCalories));//sets textview to the total calculated
        return totalCalories;
    }

    public int totalProtein(){
        Cursor data = db.getProteinData();
        int totalProtein =0;
        while (data.moveToNext()){
            totalProtein += data.getInt(0);//gets column 0 (first column) moves to next record and adds to total
        }
        Log.d(TAG, "total protein is ..." + totalProtein);
        //setProtein.setText(Integer.toString(totalProtein));
        return  totalProtein;
    }

    public void reloadFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(NutritionFragment.this).attach(NutritionFragment.this).commit();//re-loads the fragment
    }
    public double profileData(double remainingCalories, double remainingProtein){
        Cursor data = db.getNutritionProfile();//gets correct columns for the nutrition calculation on this fragment
        int col = data.getColumnCount();
            //Log.d(TAG, Integer.toString(data.getColumnCount()));
        if(data.getCount()!=0) {
            ArrayList<String> arr = new ArrayList<>();
            while (data.moveToNext()) {

                int index = data.getColumnIndexOrThrow("Age");
                String age = Integer.toString(data.getInt(index));
                arr.add(age);

                index = data.getColumnIndexOrThrow("Height");
                String height = Integer.toString(data.getInt(index));
                arr.add(height);

                index = data.getColumnIndexOrThrow("Weight");
                String weight = Integer.toString(data.getInt(index));
                arr.add(weight);

                index = data.getColumnIndexOrThrow("Gender");
                String gender = data.getString(index);
                arr.add(gender);

                index = data.getColumnIndexOrThrow("Activity");
                String activity = data.getString(index);
                arr.add(activity);

                index = data.getColumnIndexOrThrow("Gains");
                String gains = data.getString(index);
                arr.add(gains);
            }
            for (int i = 0; i < arr.size(); i++) {//iterates through arr to check values have been added correctly
                String test = arr.get(i);
                Log.i(TAG, test);
            }
          double BMR = calculateCaloriesLeft(arr, remainingCalories);//calculates the user's BMR and updates the field for user to see
            calculateProteinLeft(arr, remainingProtein);//calculates the remaining protein and updates the field for user to see
           // setRemainingCalories.setText(Double.toString(calories - remainingCalories));
            return BMR;
        }
        return remainingCalories;
    }

    public Double calculateCaloriesLeft (ArrayList<String> profile, Double remainingCalories){
        int age = Integer.parseInt(profile.get(0));
        int height = Integer.parseInt(profile.get(1));
        int weight = Integer.parseInt(profile.get(2));
        String gender = profile.get(3);//gets data for each section
        String activity = profile.get(4);
        String gains = profile.get(5);
        double caloriesLeft = 0;
        Log.i(TAG, gender + " " + activity + " " + gains);
        double BMR = 0;
        prefs = getContext().getSharedPreferences(ShareName, Context.MODE_PRIVATE);

        if(prefs.contains("Calories")){
            BMR = switchStatement(gender,activity, caloriesLeft, height, age, weight);
            caloriesLeft = Double.valueOf(prefs.getFloat("Calories",0.0f));
        }
        else {
            BMR = switchStatement(gender,activity, caloriesLeft, height, age, weight);
            caloriesLeft = switchStatement(gender,activity, caloriesLeft, height, age, weight);
            switch (gains) {
                case "Slow Gains (0.5lbs a week)":
                    caloriesLeft += 250;
                    break;
                case "Standard Gains (1lb a week)":
                    caloriesLeft += 500;
                    break;
                case "Faster Gains (1.5lbs a week)s":
                    caloriesLeft += 750;
                    break;
                case "Extreme Gains (2lbs a week)":
                    caloriesLeft += 1000;
                    break;
            }
        }
        setRemainingCalories.setText(String.valueOf((int)(caloriesLeft - remainingCalories)));
        return  BMR;
       }

       public double switchStatement(String gender, String activity, Double caloriesLeft, int height, int age, int weight){

        switch (gender) {
               case "Male":
                   caloriesLeft = ((10 * weight) + (6.25 * height) - (5 * age) + 5);//male mifflin st jeor equation
                   break;
               case "Female":
                   caloriesLeft = ((10 * weight) + (6.25 * height) - (5 * age) - 161);//female mifflin st jeor
                   break;
           }

           switch (activity) {
               case "Sedentary (little to no exercise)"://these string values are copied from the spinner string values
                   caloriesLeft = caloriesLeft * 1.2;
                   break;
               case "Lightly Active (intense exercise once or twice a week)":
                   caloriesLeft = caloriesLeft * 1.375;
                   break;
               case "Moderately Active (intense exercise 3 to 4 times a week)":
                   caloriesLeft = caloriesLeft * 1.550;
                   break;
               case "Very Active (intense exercise 5 to 7 times a week)":
                   caloriesLeft = caloriesLeft * 1.725;
                   break;
               case "Extremely Active (very active + a physically demanding daily schedule)":
                   caloriesLeft = caloriesLeft * 1.9;
                   break;
           }
           return caloriesLeft;
       }
    public  void calculateProteinLeft (ArrayList<String> profile, double remainingProtein){
        //Recommended 1.4 to 2g per kg body weight for strength training
        double proteinLeft;
        int weight = Integer.parseInt(profile.get(2));
        proteinLeft = weight *1.7;
        setRemainingProtein.setText(String.valueOf((int)(proteinLeft - remainingProtein)) + "g");
    }

    private void showAddItemDialog(final Context context) {
        final EditText taskEditText = new EditText(context);
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        taskEditText.setHint("Weight (kg)");
        AlertDialog dialog = new AlertDialog.Builder(context)//code pulled from https://alvinalexander.com/source-code/android-mockup-prototype-dialog-text-field
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



}


