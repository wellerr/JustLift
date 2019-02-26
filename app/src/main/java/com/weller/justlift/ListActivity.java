package com.weller.justlift;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    ProfileDB myDB;

    private ListView listView;
    //this class is used to create a list of the user's profile data to check when testing the app during development, will be deleted in stable version

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);//sets display to list layout xml
        listView = findViewById(R.id.listView); //finds the list view from xml file
        myDB = new ProfileDB(this);//instantiates database from profileDB

        populateListView();//calls populatelistview method, this populates listview with entries from user profile
    }

    public void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the List View.");//log used to test if method is running correctly

        Cursor data = myDB.getProfileData();//cursor used to iterate through database records
        ArrayList<String> listData = new ArrayList<>();//creates arraylist to read into the listview
        int col = data.getColumnCount();//counts number of columns from table in db
        String column = Integer.toString(col);//this is created to display in logs how many columns code detects for testing

        Log.d(TAG, column);//logs column count
        while (data.moveToNext()) {//cursor moves through db table
            for(int i=1; i<col; i++) {//iterates through columns retrieving user data from each
                listData.add(data.getString(i));//each column it adds the record to the arraylist
            }
            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);//sets the adapter to be the simple prebuilt android one, 1 row each item, adds arraylist to this format
            listView.setAdapter(adapter);//sets the above declared adapter to the list view, results in values being displayed on screen
        }
    }
}