package com.weller.justlift;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    ProfileDB myDB;
    TextView nameTextView;

    private Boolean isFirst;

    public static int displayWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        displayWidth = (int) (metrics.widthPixels * 0.6); //gets the width of user screen to use in dialog boxes, makes the width 60% of the screen width (makes it smaller box)

        myDB = new ProfileDB(getApplicationContext());//initialises the database class
        Cursor profileData = myDB.getProfileData();//gets the profile data stored in the app
        if(profileData!=null && profileData.getCount()>0){//if user has already got profile data stored in the app...
           String calories = "null";
            if(getIntent().hasExtra("UpdatedCalories")){//Looks to see if updated calories have been calculated by machine learning algorithm
                calories = getIntent().getStringExtra("UpdatedCalories"); // if it does have updated calories, this is stored in string calories
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new NutritionFragment()).commit();//Opens app straight into nutrition fragment which is my main activity
        }
        else{//if profile data hasn't been created yet...
            Intent intent = new Intent(MainActivity.this, SignUp1.class);//Sets sign up class to replace the main activity
            startActivity(intent);//starts sign up class so profile data can be created
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//creates a toolbar to be used as a navigation method through the app

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);//sets nav view as the resource to navigate through the app
        navigationView.setNavigationItemSelectedListener(this); //listens for clicks in the drawer menu

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);//creates a toggle for opening and closing the nav drawer which makes it slide in and out of visibility

        drawer.addDrawerListener(toggle);//adds 'toggle' to the nav drawer
        toggle.syncState();
        //oncreate will open homefragment
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) { //switch statement finds the clicked menu item by the id
            case R.id.nav_workout:
                //code executed when id workout clicked
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WorkoutFragment()).commit();//when nav_workout is selected, replaces the current fragment with workout fragment
                break;//once clicked, executed and then continues
            case R.id.nav_nutrition:
                //code executed when id nutrition clicked
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NutritionFragment()).commit();//when nav_nutrition is selected, replaces the current fragment with workout fragment
                break;//once clicked, executed and then continues
            case R.id.nav_profile:
                //code executed when id profile clicked
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();//when nav_profile is selected, replaces the current fragment with workout fragment
                break;//once clicked, executed and then continues
            case R.id.nav_progress:
                //code executed when id profile clicked
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProgressFragment()).commit();//when nav_progress is selected, replaces the current fragment with workout fragment
                break;//once clicked, executed and then continues

        }
        drawer.closeDrawer(GravityCompat.START);//once selected, drawer closes (if not stated it remains open)
        return true; // selects the correct item when clicked
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);//If the drawer is open and back function occurs (swipes left or taps away from nav bar) the nav bar closes
        } else {
            super.onBackPressed();
        }
    }


/*    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
   */
  }
