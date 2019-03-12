package com.weller.justlift;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class LinearRegression extends AppCompatActivity {//     This class performs linear regression on new weight data to find an updated amount of calories for user next week

    String TAG = "LinearRegression";
    TextView caloriesText;
    ProfileDB db;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updated_nutrition);
        Intent i = getIntent();
        double userBMR = i.getDoubleExtra("Calories", 0);//gets basal metabolic rate calc for the user

        Log.i(TAG, "USER BMR " + Double.toString(userBMR));//logs for testing
        caloriesText = findViewById(R.id.caloriesUpdate);//initialises text for updated calories to be put into
        double calories = userBMR;
        db = new ProfileDB(getApplicationContext());//initialise db

        Cursor c = db.getTable(ProfileDB.Table_5);//gets weekly calories and weekly weight
        Cursor z;
        Cursor j = db.getTable(ProfileDB.Table_1);//gets the profile information
        int x = c.getCount();//use this number to compare current weight to last weeks
        int lastweek;
        int currentweek;//ints set up for last and current week
        final String caloriesNeeded;
        double weightChange;
        double rateChange = 0;
        j.moveToFirst();//moves cursor to start of the profile table
        String activity = j.getString(8);//column 8 is the column for the activity column of the profile

        Log.i(TAG, activity);//logs for testing
        switch(activity) {//runs code based on what activity level user profile shows
            case "Slow Gains (0.5lbs a week)":
                rateChange = 0.5;//sets rate change double to what user set on their profile
                break;
            case "Standard Gains (1lb a week)":
                rateChange = 1;
                break;
            case "Faster Gains (1.5lbs a week)s":
                rateChange = 1.5;
                break;
            case "Extreme Gains (2lbs a week)":
                rateChange = 2;
                break;
        }
        Log.i(TAG, "x is " + Integer.toString(x));//log for testing
        if (x == 1) {//if count is 1 (first week of results)
            c.moveToFirst();//move to start of weekly food table
            double firstWeightChange = c.getDouble(2);//column for weekly weight
            double Calories = c.getInt(1);//column for weekly calories consumed
            j.moveToFirst();//move to start of profile table
            double originalWeight = j.getDouble(5);//column for profile weight (this is used as its the only weight can use at first linear regression run)
            weightChange = 2.2 * (firstWeightChange - originalWeight);//finds weight change converts to lbs

            //this has to be done as no records to compare in weekly weight so starting
            // weight from profile table is used

            Log.i(TAG, "first weight change = " + weightChange);//log for testing
            double weeklyBMR = userBMR * 7;//weekly amount of calories required set to BMR * 7
            double[] l = {Calories, weeklyBMR, weeklyBMR + 1750, weeklyBMR + 3500, weeklyBMR + 5250, weeklyBMR + 7000};//initial training data setup using 3500kcal rule
            double[] y = {weightChange, 0, 0.5, 1, 1.5, 2};//3500kcal rule
            db.addFirstLinearRegression(l, y);//runs first linear regression method from ProfileDB
            //z =  db.getTable(db.Table_LinearRegression); //can use for viewing table
            caloriesNeeded = Calculate(db, rateChange);//runs calculate method by entering db class and rate change desired by user weight
            caloriesText.setText(caloriesNeeded);//sets the calories needed to text on page
        }
        /*if more than 1 week passed, compares past data from the
           weekly table rather than reading from profile table
         */
        else {//this adds correct data to linear regression table (if there is previous data)
            lastweek = x - 2;//if more than 1 week, last week is previous row.
            Log.i(TAG, Integer.toString(lastweek));//logs for testing
            currentweek = x - 1;//current week is set as x-1
            Log.i(TAG, Integer.toString(currentweek));//logs for testing
            c.moveToPosition(lastweek);//moves to last week
            int lastweekWeight = c.getInt(2);//gets weight from last week
            c.moveToPosition(currentweek);//moves to this weeks weight
            int currentWeight = c.getInt(2);//gets last 2 entries of weight from the user
            //if first weight change log, add the standard linear regression data to table
            int currentCalories = c.getInt(1);//gets the calories eaten in last week
            weightChange = 2.2 * (currentWeight - lastweekWeight);//works out weight change in last week in lbs
            db.addLinearRegression(currentCalories, weightChange);//runs add linear regression method which adds these variables to the table (training data)
            // Log.i(TAG, Integer.toString(lastweekWeight) + " " + Integer.toString(currentWeight));
            caloriesNeeded = Calculate(db, rateChange);//calls calculate method to find out calories needed
            caloriesText.setText(caloriesNeeded);//sets text to the calories needed from linear regression
        }

        int timeOut = 3000;//sets 3 second timeout for displaying the new data
        Handler handler = new Handler();// this handler allows app to wait 3 seconds
        // to allow loading splash screen before going to calc
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(LinearRegression.this, MainActivity.class);//runs main activity class after timeout has occured
                intent.putExtra("UpdatedCalories", caloriesNeeded);//puts updated calorie requirement in shared prefs for nutrition fragment to read and update
                startActivity(intent);
            }
        }, timeOut);//the time delay set

    }

    public String Calculate(ProfileDB db, double rateChange) {
        //This method performs linear regression
        // Y = a + bX
        double intercept, slope;//intercept is a, slope is b

        ArrayList<Double> x = new ArrayList<>();//arraylist setup for x values
        ArrayList<Double> y = new ArrayList<>();//arraylist setup for y values

           Cursor c = db.getLinearRegression();//gets linear regression table from database
	        while(c.moveToNext()){//move through linear regression table
	          x.add(c.getDouble(2));//calories eaten in week
	          y.add(c.getDouble(1));//weight change in week
	          Log.i("table values", "x = " + Double.toString(c.getDouble(1)) + " y = " + Double.toString(c.getDouble(2)));
	        }

        int n = x.size();//calculate size of arraylists (x and y will be the same)
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;//variables setup to do the sums in linear regression
        for (int i = 0; i < n; i++) {
            sumx += x.get(i);//sum of x values
            sumx2 += x.get(i) * x.get(i);//sum of squares
            sumy += y.get(i);//sum of y values
        }
        double xbar = sumx / n; //mean x value
        double ybar = sumy / n; //mean y value

        double xxbar = 0.0, xybar = 0.0;//initialises more variables
        for (int i = 0; i < n; i++) {
            xxbar += (x.get(i) - xbar) * (x.get(i) - xbar);//sum of all square of the difference between x value and x mean
            xybar += (x.get(i) - xbar) * (y.get(i) - ybar);//sum of all square of the difference between y value and y mean
        }
        slope = xybar / xxbar;//b can be calculated by dividing square of differences of x and y
        intercept = ybar - slope * xbar; //a can be calculated rearranging to a = Y - bX

        //once slope and intercept have been calculated, can use these to predict calories needed for weight change goal
        DecimalFormat value = new DecimalFormat("#.##");//sets format to 2dp

        double doubleCaloriesNeeded = ((rateChange - intercept)/slope)/7;// y=mx+c rearranged to m = y-c/x to calculate calories for week, then divided by 7
        int caloriesNeeded = (int) Math.round(doubleCaloriesNeeded);//rounds calories to an int
        Log.i("Linear Regression", value.format(caloriesNeeded));//logs exact value for testing
        return value.format(caloriesNeeded);//returns calories needed
    }
}