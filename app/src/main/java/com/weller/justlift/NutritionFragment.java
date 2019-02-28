package com.weller.justlift;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

//this class is responsible for the nutrition fragment in the app
public class NutritionFragment extends Fragment {
    private String TAG = "NutritionFragment";
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
        View v = inflater.inflate(R.layout.fragment_nutrition, container, false);//on class creation, sets view to display fragment_nutrition
        addMeal = v.findViewById(R.id.addMealButton);//declares add meal button
        completeDay = v.findViewById(R.id.completeButton);//declares complete day button
        db = new ProfileDB(getContext());//instantiates database class

        mActivity = getActivity();//stores getactivity as a activity variable as has multiple uses in the class

        //below declares the various other elements of the fragment
        listView = v.findViewById(R.id.listView);
        setCalories = v.findViewById(R.id.setCalories);
        setProtein = v.findViewById(R.id.setProtein);
        setRemainingCalories = v.findViewById(R.id.setRemainingCalories);
        setRemainingProtein = v.findViewById(R.id.setRemainingProtein);
        populateListView();//runs populatelistview method, inserts data into the list view
        prefs = getContext().getSharedPreferences(ShareName, Context.MODE_PRIVATE);//gets shared preferences to see past data stored in the application
        editor = prefs.edit();//declares a preferences editor

        if (mActivity.getIntent().hasExtra("UpdatedCalories")) {//checks to see if updated calories are in the shared preferences (this is true if linear regression has just happened)
          //If LinearRegression has happened, updated calories used instead of caluclated BMR
            editor.putFloat("Calories", Float.valueOf(mActivity.getIntent().getStringExtra("UpdatedCalories")));
            editor.apply();//Adds the calories extra passed from linear regression class to the shared prefs of the app
        }
        double totalCalories = totalCalories();
        setCalories.setText(String.valueOf((int) totalCalories));//sets textview to the total calculated
        double totalProtein = totalProtein();
        setProtein.setText(String.valueOf((int) totalProtein) + "g");//sets textview to total protein calculated
        caloriesLeft = profileData(totalCalories, totalProtein); //runs profileData method which is used to calculate the remaining calories required for the user


        addMeal.setOnClickListener(new View.OnClickListener() {//sets on click listener for add meal button
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), com.weller.justlift.AddMeal.class);//if clicked, addmeal class opens in a dialog box on the fragment
                startActivityForResult(intent, 10001);//expects to get a result code when activity runs successfully
            }
        });

        completeDay.setOnClickListener(new View.OnClickListener() {//sets on click listener for complete day button
            @Override
            public void onClick(View v) {
                completeDayFunction();//runs completedayfunction method when this button is pressed (no dialog box required here)
            }
        });

        return v; //returns view v to oncreate
    }

    public void onActivityResult(int requestCode, int resultCode, Intent Data) {//once add meal has finished this class is ran
        super.onActivityResult(requestCode, resultCode, Data);//requestcode, resultcode and data returned from class
        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK)) {//if request id's match (100001 from the add meal class) and result ok returned
            reloadFragment();//reloadfragment class is ran, this refreshes the page to the newest state
        }

    }

    public void completeDayFunction() { //code that completes day of nutrition for the user
        Log.i("complete day", "Daycount is " + dayCount);//log used to test, this creates a log with number of days passed, used in testing to check if linear regression occurs after week
        dayCount = db.getDayCount(getContext());//gets day count before running conditional statement (fixed bug)
        if ((dayCount % 7 != 0) || (dayCount == 0)) {//if not end of the week
            dayCount++;//increments days
            db.updateDayCount(getContext(), dayCount);//updates shared prefs with new amount of days (this enables number to carry over across multiple user sessions which was a problem initially in testing)
            db.deleteNutritionData(dayCount);//deletes nutrition data, adds nutrition data to the past meals table with the day count as a column
            reloadFragment();//reloadfrgment method caled to refresh page
        } else {
            //if end of week ask for user to enter weight
            Intent intent = new Intent(getContext(), AddWeeklyWeight.class);//add weight class run in dialog box waiting for user input
            intent.putExtra("caloriesLeft", caloriesLeft);//passes caloriesleft into the next class
            Log.i(TAG, Double.toString(caloriesLeft));//logs calories left for testing
            startActivity(intent);//starts the add weight class
            //then sum the past meals table for calories and put into weekly
            //then clear past meals table
        }
    }

    private void toastMessage(String message) {//method used to produce toast messages on app screen for testing
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void populateListView() {//method responsible for populating the 3 column list view in the app
        Log.d(TAG, "populateListView: Displaying data in the List View.");
        Cursor data = db.getTable(ProfileDB.Table_2);//gets the nutrition data table from the database
        listData = new ArrayList<>();//creates arraylist
        int col = data.getColumnCount();//counts columns in cursor
        String column = Integer.toString(col);
        int i = 0;
        while (data.moveToNext()) {//cursor moves through db
            nutrition = new NutritionData(data.getString(1), data.getString(2), data.getString(3));//gets string variable at each column from nutrition data class
            String dataAdded = "Data added " + data.getString(1) + " " + data.getString(2) + " " + data.getString(3);//string created to collect data added to each row
            Log.d(TAG, dataAdded);//logs each row for testing
            listData.add(i, nutrition);//adds each row in the necessary columns
            Log.d(TAG, "List adapter starting...");//logs list adapter starting used in testing
            NutritionAdapter adapter = new NutritionAdapter(getContext(), R.layout.adapter_view, listData);//puts 3 variables from listdata into the adapter_view.xml in my layout (sets to 3 columns)
            listView.setAdapter(adapter);//views the new table (on screen this shows up as a new row)
            i++;//increments i so next row is done next
        }
    }

    public int totalCalories() {
        Cursor data = db.getCaloriesData();//gets calories data from the database
        int totalCalories = 0;//sets initial total to 0
        while (data.moveToNext()) {//iterates through calories data
            totalCalories += data.getInt(0);//gets column 0 (first column) moves to next record and adds to total until runs out of records
        }
        Log.d(TAG, "total calories is ..." + totalCalories);//logs total calories each time its totalled for testing
        //  setCalories.setText(Integer.toString(totalCalories));//sets textview to the total calculated
        return totalCalories;
    }

    public int totalProtein() {//same function as above but for protein
        Cursor data = db.getProteinData();
        int totalProtein = 0;
        while (data.moveToNext()) {
            totalProtein += data.getInt(0);//gets column 0 (first column) moves to next record and adds to total
        }
        Log.d(TAG, "total protein is ..." + totalProtein);
        //setProtein.setText(Integer.toString(totalProtein));
        return totalProtein;
    }

    public void reloadFragment() {//refreshes the fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(NutritionFragment.this).attach(NutritionFragment.this).commit();//re-loads the fragment by detaching and attaching same one
    }

    public double profileData(double remainingCalories, double remainingProtein) {
        Cursor data = db.getNutritionProfile();//gets correct columns for the nutrition calculation on this fragment
        int col = data.getColumnCount();
        //Log.d(TAG, Integer.toString(data.getColumnCount()));
        if (data.getCount() != 0) {
            ArrayList<String> arr = new ArrayList<>();
            while (data.moveToNext()) {
                int index = data.getColumnIndexOrThrow("Age");
                String age = Integer.toString(data.getInt(index));
                arr.add(age);//adds age to the array

                index = data.getColumnIndexOrThrow("Height");
                String height = Integer.toString(data.getInt(index));
                arr.add(height);//adds height to the array

                index = data.getColumnIndexOrThrow("Weight");
                String weight = Integer.toString(data.getInt(index));
                arr.add(weight);//adds weight to the array

                index = data.getColumnIndexOrThrow("Gender");
                String gender = data.getString(index);
                arr.add(gender);//adds gender to the array

                index = data.getColumnIndexOrThrow("Activity");
                String activity = data.getString(index);
                arr.add(activity);//adds activity to the array

                index = data.getColumnIndexOrThrow("Gains");
                String gains = data.getString(index);
                arr.add(gains);//adds gains to the array
            }
            for (int i = 0; i < arr.size(); i++) {//iterates through arr to check values have been added correctly
                String test = arr.get(i);
                Log.i(TAG, test);//logs array contents for testing
            }
            double BMR = calculateCaloriesLeft(arr, remainingCalories);//calculates the user's BMR by sending array to calcuulatecaloriesleft method  and updates the field for user to see
            calculateProteinLeft(arr, remainingProtein);//calculates the remaining protein by sending array to calculateproteinleft and updates the field for user to see
            // setRemainingCalories.setText(Double.toString(calories - remainingCalories));
            return BMR; //BMR returned for use in calculations
        }
        return remainingCalories;//if the if statement doesn't run, this variable is returned instead
    }

    public Double calculateCaloriesLeft(ArrayList<String> profile, Double remainingCalories) {
        int age = Integer.parseInt(profile.get(0));
        int height = Integer.parseInt(profile.get(1));
        int weight = Integer.parseInt(profile.get(2));
        String gender = profile.get(3);//gets data for each section
        String activity = profile.get(4);
        String gains = profile.get(5);
        //gets profile information for the user
        double caloriesLeft = 0;//initialises calories at 0
        Log.i(TAG, gender + " " + activity + " " + gains);
        double BMR = 0;
        prefs = getContext().getSharedPreferences(ShareName, Context.MODE_PRIVATE);

        if (prefs.contains("Calories")) {//if calories already stored in the shared prefs, uses this for calories left variable
            BMR = switchStatement(gender, activity, caloriesLeft, height, age, weight);
            caloriesLeft = Double.valueOf(prefs.getFloat("Calories", 0.0f));
        } else {//if calories not in shared prefs, uses the BMR which is the amount needed before any food is eaten
            BMR = switchStatement(gender, activity, caloriesLeft, height, age, weight);//first finds the BMR
            caloriesLeft = switchStatement(gender, activity, caloriesLeft, height, age, weight);//CHANGE TO caloriesleft = BMR
            switch (gains) {//this switch statement adds a specific amount depending on user goal based on 3500kcal per 1lb rule
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
        setRemainingCalories.setText(String.valueOf((int) (caloriesLeft - remainingCalories)));//sets the text to caloriesLeft - remainingCalories to give user how much they have left to eat
        return BMR;
    }

    public double switchStatement(String gender, String activity, Double caloriesLeft, int height, int age, int weight) {
        //This method calculates a person's BMR using harris-benedict equation
        switch (gender) {//switch statement checks all profile entries to find their specific 'caloriesLeft'
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
        return caloriesLeft;//returns amount of calories required
    }

    public void calculateProteinLeft(ArrayList<String> profile, double remainingProtein) {
        //Recommended 1.4 to 2g per kg body weight for strength training
        double proteinLeft;
        int weight = Integer.parseInt(profile.get(2));//gets users weight
        proteinLeft = weight * 1.7;//multiply by value inbetween 1.4 and 2
        setRemainingProtein.setText(String.valueOf((int) (proteinLeft - remainingProtein)) + "g");//sets text to this calculated value
    }
}


