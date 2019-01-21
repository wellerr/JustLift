package com.weller.justlift;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    ProfileDB db;
    String tableName = "Profile_table";

    TextInputEditText firstNameEditText;
    TextInputEditText surnameEditText;
    TextInputEditText ageEditText;
    TextInputEditText heightEditText;
    TextInputEditText weightEditText;

    Spinner spinner_gender;
    Spinner spinner_activity;
    Spinner spinner_gains;

    String firstNameString;
    String surnameString;
    String ageString;
    String heightString;
    String weightString;
    String activityString;
    String genderString;
    String gainsString;


    @Override



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
    /*issue i was having was not saving fragment as view and returning
       I needed to use this view as the context for the spinner as seen below
     */
        firstNameEditText = v.findViewById(R.id.text_firstname);
        surnameEditText = v.findViewById(R.id.text_surname);
        ageEditText = v.findViewById(R.id.text_age);
        heightEditText = v.findViewById(R.id.text_height);
        weightEditText = v.findViewById(R.id.text_weight);


        spinner_gender = v.findViewById(R.id.spinner_gender);
        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(v.getContext(), R.array.sex, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter_gender);
        spinner_gender.setOnItemSelectedListener(this);
        // this spinner is for the genderString of the user

        spinner_activity = v.findViewById(R.id.spinner_activity);
        ArrayAdapter<CharSequence> adapter_activity = ArrayAdapter.createFromResource(v.getContext(), R.array.activity, android.R.layout.simple_spinner_item);
        adapter_activity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_activity.setAdapter(adapter_activity);
        spinner_activity.setOnItemSelectedListener(this);


        spinner_gains = v.findViewById(R.id.spinner_gains);
        ArrayAdapter<CharSequence> adapter_gains = ArrayAdapter.createFromResource(v.getContext(), R.array.gains, android.R.layout.simple_spinner_item);
        adapter_activity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gains.setAdapter(adapter_gains);
        spinner_gains.setOnItemSelectedListener(this);

        Button saveButton = (v.findViewById(R.id.submitProfile));
        Button viewButton = (v.findViewById(R.id.viewData));

        db = new ProfileDB(getContext());

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                firstNameString =   firstNameEditText.getText().toString();
                surnameString =   surnameEditText.getText().toString();
                ageString =   ageEditText.getText().toString();
                heightString =   heightEditText.getText().toString();
                weightString =   weightEditText.getText().toString();
                genderString = spinner_gender.getSelectedItem().toString();
                activityString = spinner_activity.getSelectedItem().toString();
                gainsString = spinner_gains.getSelectedItem().toString();
                if(firstNameString.length()!=0 && surnameString.length()!=0 && ageString.length()!=0 && heightString.length()!=0 &&
                        weightString.length()!=0 && genderString.length()!=0 && activityString.length()!=0 && gainsString.length()!=0){//checks if all fields have been entered
                   AddData(firstNameString, surnameString, ageString, heightString, weightString, genderString, activityString, gainsString); //adds fields to database
                }
                else{
                    toastMessage("Enter data");
                }
            }
        });
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), com.weller.justlift.ListActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
    public void AddData(String firstName, String surname, String age, String height, String weight, String gender, String activity, String gains) {
        boolean insertData = db.addProfileData(firstName, surname, age, height, weight, gender, activity, gains);
        if (insertData) {
            toastMessage("Test successful");
        }
        else {
            toastMessage("Test successful ver 2");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
