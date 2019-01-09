package com.weller.justlift;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    ProfileDB db;
  //  SQLiteDatabase db;
   // SQLiteOpenHelper openHelper;
   // SQLite sql;

    TextInputEditText firstNameEditText;
    TextInputEditText surnameEditText;
   // Cursor cursor;

    @Override



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
    /*issue i was having was not saving fragment as view and returning
       I needed to use this view as the context for the spinner as seen below
     */
        firstNameEditText = v.findViewById(R.id.text_firstname);
        surnameEditText = v.findViewById(R.id.text_surname);
        // TextInputEditText ageEditText = v.findViewById(R.id.text_age);
        // TextInputEditText weightEditText = v.findViewById(R.id.text_weight);


        Spinner spinner_sex = v.findViewById(R.id.spinner_sex);
        ArrayAdapter<CharSequence> adapter_sex = ArrayAdapter.createFromResource(v.getContext(), R.array.sex, android.R.layout.simple_spinner_item);
        adapter_sex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sex.setAdapter(adapter_sex);
        spinner_sex.setOnItemSelectedListener(this);
        // this spinner is for the sex of the user

        Spinner spinner_activity = v.findViewById(R.id.spinner_activity);
        ArrayAdapter<CharSequence> adapter_activity = ArrayAdapter.createFromResource(v.getContext(), R.array.activity, android.R.layout.simple_spinner_item);
        adapter_activity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_activity.setAdapter(adapter_activity);
        spinner_activity.setOnItemSelectedListener(this);


        Spinner spinner_gains = v.findViewById(R.id.spinner_gains);
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

                String newEntry =   firstNameEditText.getText().toString();
                if(newEntry.length()!=0){
                   AddData(newEntry); //
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



    public void AddData(String newEntry) {
      //  boolean insertData = true;//db.addData(newEntry);


        boolean insertData = db.addData(newEntry);
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
      //  Toast.makeText(parent.getContext(),text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
