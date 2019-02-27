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

public class SignUp1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextInputEditText firstNameEditText;
    TextInputEditText surnameEditText;
    TextInputEditText ageEditText;
    TextInputEditText heightEditText;
    TextInputEditText weightEditText;


    Spinner spinner_gender;
    Spinner spinner_activity;
    Spinner spinner_gains;

    Button nextButton;
    Button submitButton;


    String firstNameString;
    String surnameString;
    String ageString;
    String heightString;
    String weightString;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);//sets layout as activity_signup1

        firstNameEditText = findViewById(R.id.text_firstname);
        surnameEditText = findViewById(R.id.text_surname);
        ageEditText = findViewById(R.id.text_age);
        heightEditText = findViewById(R.id.text_height);
        weightEditText = findViewById(R.id.text_weight);
        nextButton = findViewById(R.id.nextButton);
        nextButton = findViewById(R.id.nextButton);
        //initialises variables for each of the sections on page 1 for sign up

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameString = firstNameEditText.getText().toString();
                surnameString = surnameEditText.getText().toString();
                ageString = ageEditText.getText().toString();
                heightString = heightEditText.getText().toString();
                weightString = weightEditText.getText().toString();
                //converts all of the inputs to strings for storage
                if(firstNameString.length()!=0 && surnameString.length()!=0 && ageString.length()!=0 &&
                        heightString.length()!=0 && weightString.length()!=0) {//if all fields have been filled...
                    Intent intent = new Intent(SignUp1.this, SignUp2.class);//changes screen to second page of sign up
                    intent.putExtra("firstNameString", firstNameString);
                    intent.putExtra("surnameString", surnameString);
                    intent.putExtra("ageString", ageString);
                    intent.putExtra("heightString", heightString);
                    intent.putExtra("weightString", weightString);//puts the string variables into shared prefs for access on next class
                    startActivity(intent);
                }
                else{
                    toastMessage("Enter data");//if one field isnt filled, app prompts user to fill
                }
            }
        });

    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
