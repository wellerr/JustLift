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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        listView = findViewById(R.id.listView);
        myDB = new ProfileDB(this);

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the List View.");

        Cursor data = myDB.getData();
        ArrayList<String> listData = new ArrayList<>();
        int col = data.getColumnCount();
        String column = Integer.toString(col);

        Log.d(TAG, column);
        while (data.moveToNext()) {//cursor moves through db
            for(int i=1; i<col; i++) {//iterates through columns retrieving user data
                listData.add(data.getString(i));
            }

            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
            listView.setAdapter(adapter);
        }
    }
}