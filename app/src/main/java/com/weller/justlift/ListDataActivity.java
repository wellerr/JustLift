package com.weller.justlift;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";
    ProfileDB db;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        listView = (ListView) findViewById(R.id.listView);
        db = new ProfileDB(this);
        
        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in list view.");
        //get data and append to list
        Cursor data = db.getData();

        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            listData.add(data.getString(1));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
    }

}
