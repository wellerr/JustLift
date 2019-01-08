package com.weller.justlift;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    ProfileDB myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this); //listens for clicks in the drawer menu

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        myDB = new ProfileDB(this);//this isn't working yet, table isnt creating
        //oncreate will open homefragment

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){ //switch statement finds the clicked menu item by the id
            case R.id.nav_home:
                //code executed when id workout clicked
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;//once clicked, executed and then continues
            case R.id.nav_workout:
                //code executed when id workout clicked
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WorkoutFragment()).commit();
                break;//once clicked, executed and then continues
            case R.id.nav_nutrition:
                //code executed when id nutrition clicked
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NutritionFragment()).commit();
                break;//once clicked, executed and then continues
            case R.id.nav_profile:
                //code executed when id profile clicked
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;//once clicked, executed and then continues
            case R.id.nav_progress:
                //code executed when id profile clicked
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProgressFragment()).commit();
                break;//once clicked, executed and then continues

        }

        drawer.closeDrawer(GravityCompat.START);//once selected, drawer closes (if not stated it remains open)

        return true; // selects the correct item when clicked
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
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
