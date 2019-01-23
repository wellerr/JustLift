package com.weller.justlift;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
    TextView getSetRemainingProtein;

    ArrayList<NutritionData> listData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nutrition, container, false);
        addMeal = v.findViewById(R.id.addMealButton);
        completeDay = v.findViewById(R.id.completeButton);
        db = new ProfileDB(getContext());
        listView = v.findViewById(R.id.listView);
        setCalories = v.findViewById(R.id.setCalories);
        setProtein = v.findViewById(R.id.setProtein);
        setRemainingCalories = v.findViewById(R.id.setRemainingCalories);
        populateListView();

        double totalCalories = totalCalories();
        setCalories.setText(Double.toString(totalCalories));//sets textview to the total calculated

        double totalProtein = totalProtein();
        setProtein.setText(Double.toString(totalProtein));
        profileData(totalCalories, totalProtein);


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
    public void profileData(double remainingCalories, double remainingProtein){
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
            calculateCaloriesLeft(arr, remainingCalories);
        }
    }

    public  void calculateCaloriesLeft (ArrayList<String> profile, double remainingCalories){
        int age = Integer.parseInt(profile.get(0));
        int height = Integer.parseInt(profile.get(1));
        int weight = Integer.parseInt(profile.get(2));
        String gender = profile.get(3);//gets data for each section
        String activity = profile.get(4);
        String gains = profile.get(5);
        double caloriesLeft;
        if(gender.contains("male")){
            caloriesLeft = ((10 * weight) + (6.25 * height) - (5 * age) + 5);//male mifflin st jeor equation
        }
        else{
            caloriesLeft = ((10 * weight) + (6.25 * height) - (5 * age) - 161);//female mifflin st jeor
        }

        if(activity.contains("Sedentary")){//adds activity factor
            caloriesLeft = caloriesLeft * 1.2;
        }
        else if(activity.contains("Lightly Active")){
            caloriesLeft = caloriesLeft * 1.375;
        }
        else if(activity.contains("Moderately Active")){
            caloriesLeft = caloriesLeft * 1.550;
        }
        else if(activity.contains("Very Active")){
            caloriesLeft = caloriesLeft * 1.725;
        }
        else if(activity.contains("Extremely Active")){
            caloriesLeft = caloriesLeft * 1.9;
        }

        if(gains.contains("Slow Gains")){
            caloriesLeft = caloriesLeft + 250;
        }
        else if(gains.contains("Standards Gains")){
            caloriesLeft = caloriesLeft + 500;
        }
        else if(gains.contains("Faster Gains")){
            caloriesLeft = caloriesLeft + 750;
        }
        else if(gains.contains("Extreme Gains")){
            caloriesLeft = caloriesLeft + 1000;
        }
        setRemainingCalories.setText(Double.toString(caloriesLeft - remainingCalories));
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
                        else {
                            int updatedWeight = Integer.parseInt(taskEditText.getText().toString());
                            dayCount++;//when added daycount increments
                            db.updateDayCount(getContext(), dayCount);
                            db.addToWeeklyTable(updatedWeight);
                            db.deleteNutritionData(dayCount);
                            db.deletePastMeals();
                            reloadFragment();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }



}


