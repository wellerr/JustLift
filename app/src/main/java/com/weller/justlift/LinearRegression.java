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
        double userBMR = i.getDoubleExtra("Calories", 0);

        Log.i(TAG, Double.toString(userBMR));
        caloriesText = findViewById(R.id.caloriesUpdate);
        double calories = userBMR;
        db = new ProfileDB(getApplicationContext());

        Cursor c = db.getWeeklyTable();//gets weekly calories and weekly weight
        Cursor z;
        Cursor j = db.getProfileData();
        int x = c.getCount();//use this number to compare current weight to last weeks
        int lastweek;
        int currentweek;
        double caloriesNeeded;
        double weightChange;
        if (x == 1) {
            c.moveToFirst();
            double firstWeightChange = c.getInt(2);//column for weekly weight
            double Calories = c.getInt(1);
            j.moveToFirst();
            double originalWeight = j.getInt(5);//column for profile weight
            weightChange = 2.2 * (firstWeightChange - originalWeight);//finds weight change converts to lbs

            //this has to be done as no records to compare in weekly weight so starting
            // weight from profile table is used

            Log.i(TAG, "first weight change = " + weightChange);
            double weeklyBMR = userBMR * 7;
            double[] l = {Calories, weeklyBMR, weeklyBMR + 1750, weeklyBMR + 3500, weeklyBMR + 5250, weeklyBMR + 7000};
            double[] y = {weightChange, 0, 0.5, 1, 1.5, 2};
            db.addFirstLinearRegression(l, y);
            z = db.getLinearRegression();
            caloriesNeeded = Calculate(db);
            caloriesText.setText(Double.toString(caloriesNeeded));
        }
        /*if more than 1 week passed, compares past data from the
           weekly table rather than reading from profile table
         */
        else {//this adds correct data to linear regression table (if there is previous data)
            lastweek = x - 2;//if more than 1 week, last week is previous row.
            currentweek = x - 1;
            c.moveToPosition(lastweek);
            int lastweekWeight = c.getInt(2);
            c.moveToPosition(currentweek);
            int currentWeight = c.getInt(2);//gets last 2 entries of weight from the user
            //if first weight change log, add the standard linear regression data to table
            int currentCalories = c.getInt(1);
            weightChange = 2.2 * (currentWeight - lastweekWeight);
            db.addLinearRegression(currentCalories, weightChange);
            // Log.i(TAG, Integer.toString(lastweekWeight) + " " + Integer.toString(currentWeight));
            caloriesNeeded = Calculate(db);
            caloriesText.setText(Double.toString(caloriesNeeded));
        }

        int timeOut = 3000;
        Handler handler = new Handler();// this handler allows app to wait 5 seconds
        // to allow loading splash screen before going to calc
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(LinearRegression.this, MainActivity.class);
                startActivity(intent);
            }
        }, timeOut);//the time delay set

    }

    public Double Calculate(ProfileDB db) {

        double intercept, slope;

        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();

           Cursor c = db.getLinearRegression();
	        while(c.moveToNext()){
	          x.add(c.getDouble(2));
	          y.add(c.getDouble(1));
	          Log.i("table values", "x = " + Double.toString(c.getDouble(1)) + " y = " + Double.toString(c.getDouble(2)));
	        }

        int n = x.size();

        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
        for (int i = 0; i < n; i++) {
            sumx += x.get(i);
            sumx2 += x.get(i) * x.get(i);
            sumy += y.get(i);
        }
        double xbar = sumx / n;
        double ybar = sumy / n;

        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < n; i++) {
            xxbar += (x.get(i) - xbar) * (x.get(i) - xbar);
            yybar += (y.get(i) - ybar) * (y.get(i) - ybar);
            xybar += (x.get(i) - xbar) * (y.get(i) - ybar);
        }
        slope = xybar / xxbar;
        intercept = ybar - slope * xbar;

        DecimalFormat value = new DecimalFormat("#.##");

        double caloriesNeeded = (0.5/slope) - intercept;
        Log.i("Linear Regression", value.format(caloriesNeeded));
        return  caloriesNeeded;
    }
}