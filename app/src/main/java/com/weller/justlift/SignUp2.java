package com.weller.justlift;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SignUp2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ProfileDB db;
    Spinner spinner_gender;
    Spinner spinner_activity;
    Spinner spinner_gains;

    Button submitButton;
    String firstNameString;
    String surnameString;
    String ageString;
    String heightString;
    String weightString;
    String activityString;
    String genderString;
    String gainsString;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup2);//sets layout to activity_signup2
        db = new ProfileDB(getApplicationContext());//initialises the database
        Intent i = getIntent();//gets intent for shared prefs
        firstNameString=i.getStringExtra("firstNameString");
        surnameString=i.getStringExtra("surnameString");
        ageString=i.getStringExtra("ageString");
        heightString=i.getStringExtra("heightString");
        weightString=i.getStringExtra("weightString");
        //gets all the string values from previous page from shared prefs

        submitButton = findViewById(R.id.submitButton);

        spinner_gender = findViewById(R.id.spinner_gender);
        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sex, R.layout.my_spinner);//sets spinner content and layout to my_spinner
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter_gender);
        spinner_gender.setOnItemSelectedListener(this);
        // this spinner is for the genderString of the user

        spinner_activity = findViewById(R.id.spinner_activity);
        ArrayAdapter<CharSequence> adapter_activity = ArrayAdapter.createFromResource(getApplicationContext(), R.array.activity, R.layout.my_spinner);
        adapter_activity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_activity.setAdapter(adapter_activity);
        spinner_activity.setOnItemSelectedListener(this);

        spinner_gains = findViewById(R.id.spinner_gains);
        ArrayAdapter<CharSequence> adapter_gains = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gains, R.layout.my_spinner);
        adapter_activity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gains.setAdapter(adapter_gains);
        spinner_gains.setOnItemSelectedListener(this);


        submitButton = findViewById(R.id.submitButton);
        //above gets all the information entered by the user

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//once submit is pressed by user
                genderString = spinner_gender.getSelectedItem().toString();
                activityString = spinner_activity.getSelectedItem().toString();
                gainsString = spinner_gains.getSelectedItem().toString();
                //converts all entries to strings
                if(firstNameString.length()!=0 && surnameString.length()!=0 && ageString.length()!=0 && heightString.length()!=0 &&
                        weightString.length()!=0 && genderString.length()!=0 && activityString.length()!=0 && gainsString.length()!=0){//checks if all fields have been entered
                    AddData(firstNameString, surnameString, ageString, heightString, weightString, genderString, activityString, gainsString); //adds fields to database by calling adddata method
                }
                else{
                    toastMessage("Enter data");//if a field is empty, error displayed
                }
                startActivity(new Intent(SignUp2.this, MainActivity.class));//once this had passed, main activity is started
            }
        });

    }

    public void AddData(String firstName, String surname, String age, String height, String weight, String gender, String activity, String gains) {
        boolean insertData = db.addProfileData(firstName, surname, age, height, weight, gender, activity, gains);//adds all user info to profile, returns true if successful
        if (insertData) {//if successful
            toastMessage("Test successful"); // testing logs
        }
        else {//if fails
            toastMessage("Test successful ver 2");
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();//method to create a toast message
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
