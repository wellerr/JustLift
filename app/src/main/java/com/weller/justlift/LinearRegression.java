package com.weller.justlift;

import java.text.DecimalFormat;

public class LinearRegression {

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
