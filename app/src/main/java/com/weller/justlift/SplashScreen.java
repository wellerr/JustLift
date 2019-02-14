package com.weller.justlift;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Intent i = getIntent();
        Integer code = i.getIntExtra("Code", 0);
        final Double calories = i.getDoubleExtra("Calories", 0);//gets the users needed calories from nutrition
        Log.i(TAG, Integer.toString(code));
        if (code != 1) {//If screen loads at startup
            EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                    .withFullScreen()
                    .withTargetActivity(MainActivity.class)
                    .withSplashTimeOut(1000)
                    .withBackgroundResource(R.color.colorBackground)
                  //  .withBackgroundColor(Color.parseColor("#35454D"))//colour from theme
                    .withLogo(R.mipmap.ic_launcher)
                    .withHeaderText("")//couldn't delete this line without crash?
                    //.withFooterText
                    // .withBeforeLogoText("heh")
                    ;
            config.getHeaderTextView().setTextColor(Color.WHITE);
            //set to view
            View view = config.create();
            setContentView(view);
        }
        else{//if screen loads when user logs weight after 1 week
            int timeOut = 2500;
            EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                    .withFullScreen()
                    .withTargetActivity(MainActivity.class)
                    .withSplashTimeOut(timeOut)
                    .withBackgroundResource(R.color.colorBackground)//colour from theme
                    .withLogo(R.mipmap.ic_launcher)
                    .withHeaderText("")//couldn't delete this line without crash?
                    .withBeforeLogoText("Loading Updated Nutritional Requirements...")
                    ;
            config.getHeaderTextView().setTextColor(Color.WHITE);
            //set to view
            View view = config.create();
            setContentView(view);
            Handler handler = new Handler();// this handler allows app to wait 5 seconds
                                            // to allow loading splash screen before going to calc
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, LinearRegression.class);
                    intent.putExtra("Calories", calories);//sends users needed calories to linear regression
                    startActivity(intent);
                }
            }, timeOut);//the time delay set

        }
    }
}
/*
The MIT License (MIT)
Copyright (c) 2016 Leonidas Maroulis

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
