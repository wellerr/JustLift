package com.weller.justlift;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(1000)
                //.withBackgroundResource(android.R.color.background_dark)
                .withBackgroundColor(Color.parseColor("#50000c"))//colour from theme
                .withLogo(R.mipmap.ic_launcher)
                .withHeaderText("")//couldn't delete this line without crash?
                //.withFooterText
               // .withBeforeLogoText("heh")
        ;

        //set text colour
        config.getHeaderTextView().setTextColor(Color.WHITE);

        //set to view
        View view = config.create();

        setContentView(view);

    }
}
