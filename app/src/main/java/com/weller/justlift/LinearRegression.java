package com.weller.justlift;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

public class LinearRegression extends AppCompatActivity {//     This class performs linear regression on new weight data to find an updated amount of calories for user next week

    String TAG = "LinearRegression";
    TextView caloriesText;
    ProfileDB db;

    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.updated_nutrition);
       Intent i = getIntent();
       double userBMR = i.getDoubleExtra("Calories", 0);

       Log.i(TAG, Double.toString(userBMR));
       caloriesText =  findViewById(R.id.caloriesUpdate);
       double calories = userBMR;
       db = new ProfileDB(getApplicationContext());

      Cursor c = db.getWeeklyTable();//gets weekly calories and weekly weight
      Cursor z;
      Cursor j = db.getProfileData();
      int x = c.getCount();//use this number to compare current weight to last weeks
      int lastweek;
      int currentweek;
      double weightChange;
      if(x == 1){
          c.moveToFirst();
          double firstWeightChange = c.getInt(2);//column for weekly weight
          double Calories = c.getInt(1);
          j.moveToFirst();
          double originalWeight = j.getInt(5);//column for profile weight
          weightChange = 2.2* (firstWeightChange - originalWeight);//finds weight change converts to lbs

          //this has to be done as no records to compare in weekly weight so starting
          // weight from profile table is used

          Log.i(TAG, "first weight change = " + weightChange);
          double weeklyBMR =  userBMR * 7;
          double[] l = {Calories, weeklyBMR, weeklyBMR+1750, weeklyBMR+3500, weeklyBMR+5250, weeklyBMR+7000};
          double[] y = {weightChange, 0, 0.5, 1, 1.5, 2};
          db.addFirstLinearRegression(l, y);
          z = db.getLinearRegression();
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
          caloriesText.setText(Double.toString(calories));
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
    public static void Calculate() {
        double intercept, slope;
        // double r2;
        // double svar0, svar1;

        double[] x = {17500, 19250, 21000, 22750, 24500};
        double[] y = {0, 0.5, 1, 1.5, 2};
        int n = x.length;

        // first pass
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
        for (int i = 0; i < n; i++) {
            sumx += x[i];
            sumx2 += x[i] * x[i];
            sumy += y[i];
        }
        double xbar = sumx / n;
        double ybar = sumy / n;

        // second pass: compute summary statistics
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < n; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        slope = xybar / xxbar;
        intercept = ybar - slope * xbar;
        //    System.out.println(slope + " " + intercept);

    /*     // more statistical analysis
         double rss = 0.0;      // residual sum of squares
         double ssr = 0.0;      // regression sum of squares
         for (int i = 0; i < n; i++) {
             double fit = slope*x[i] + intercept;
             rss += (fit - y[i]) * (fit - y[i]);
             ssr += (fit - ybar) * (fit - ybar);
         }

         int degreesOfFreedom = n-2;
         r2    = ssr / yybar;
         double svar  = rss / degreesOfFreedom;
         svar1 = svar / xxbar;
         svar0 = svar/n + xbar*xbar*svar1;
      */
        DecimalFormat value = new DecimalFormat("#.##");
        double caloriesDay = 2750;
        double caloriesWeek = caloriesDay * 7;
        //  System.out.print(slope*caloriesWeek + intercept);
        double weightChange = slope * caloriesWeek + intercept;
        System.out.println(value.format(weightChange));

            }
        }
