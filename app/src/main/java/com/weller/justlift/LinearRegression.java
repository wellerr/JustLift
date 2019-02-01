package com.weller.justlift;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.DecimalFormat;

public class LinearRegression extends AppCompatActivity {//     This class performs linear regression on new weight data to find an updated amount of calories for user next week

    TextView caloriesText;
    ProfileDB db;

    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.updated_nutrition);
       caloriesText =  findViewById(R.id.caloriesUpdate);
       int calories = 2500;
       caloriesText.setText(Integer.toString(calories));


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
