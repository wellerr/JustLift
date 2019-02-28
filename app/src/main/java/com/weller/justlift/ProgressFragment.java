package com.weller.justlift;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
public class ProgressFragment extends Fragment {

    private ListView listView;
    public ProfileDB db;
    private String TAG = "ProgressFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress, container, false);//sets display to fragment_progress xml
        db = new ProfileDB(getContext());//initialises ProfileDB class
        listView = v.findViewById(R.id.listView);//finds list view for exercises to be displayed on
        populateListView();//calls populatelistview method

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(),ProgressGraph.class);
                intent.putExtra("pos", position);//puts position of list view clicked into the progress graph class so that it can tell which graph to show on screen
                startActivity(intent);
              //  startActivityForResult(intent,10001);
            }
        });
        return v;
    }

    public void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the List View.");//log for testing purposes

        Cursor data = db.getTable(db.Table_4);//gets names of exercises from database
        ArrayList<String> listData = new ArrayList<>();//sets up an arraylist to store the exercises in
        int col = data.getColumnCount();//gets number of columns
        String column = Integer.toString(col);//converts number to a string

        Log.d(TAG, column);//logs column count for testing
        while (data.moveToNext()) {//cursor moves through db
            for(int i=1; i<col; i++) {//iterates through columns retrieving user data
                listData.add(data.getString(i));//adds exercise to arraylist
            }
            ListAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.my_text_view, listData);//puts arraylist into my_text_view layout
            listView.setAdapter(adapter);//displays the info on screen
        }
    }
}
