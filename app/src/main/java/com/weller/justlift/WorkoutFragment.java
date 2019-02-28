package com.weller.justlift;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;



public class WorkoutFragment extends Fragment {
    public ProfileDB db;
    private String TAG = "WorkoutFragment";
    Button addExercise;

    private ListView listView;
    private ArrayList<String> caloriesList;
    private ArrayList<String> proteinList;

    public int dayCount;

    ArrayList<String> listData = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout, container, false);//sets layout to fragment_workout
        db = new ProfileDB(getContext());//initialises the database
        addExercise = v.findViewById(R.id.addExercise);
        listView = v.findViewById(R.id.listView);//initialises button and listview

        populateListView();//calls populatelistviewmethod

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//when add exercise button clicked
                int listCount = listView.getCount();//looks at how many exercises already logged
                if (listCount <= 4) { //max number of entries -1 (listview count starts at 0) IF no more than 5 exercises
                    Intent intent = new Intent(getContext(), com.weller.justlift.AddExercise.class);//opens add exercise dialog box
                    startActivityForResult(intent, 10001);//wants result code
                }
                else{
                    Toast.makeText(getContext(), "max number of exercises", Toast.LENGTH_LONG).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//listens to position selected in the list view
                Intent intent = new Intent(view.getContext(),AddExerciseWeight.class);//opens add exercise weight dialog box
                intent.putExtra("pos", position);
                startActivityForResult(intent,10001);//wants result
            }
        });

        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent Data){//once add meal has finished...
        super.onActivityResult(requestCode, resultCode, Data);
        if((requestCode == 10001) && (resultCode == Activity.RESULT_OK)){//if request id's match and result ok returned
            reloadFragment();
        }

    }
    public void reloadFragment(){//method refreshes the fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(WorkoutFragment.this).attach(WorkoutFragment.this).commit();//re-loads the fragment
    }
    public void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the List View.");//log for testing

        Cursor data = db.getTable(ProfileDB.Table_4);//gets exercise names from db
        ArrayList<String> listData = new ArrayList<>();//creates arraylist for the exercise names
        int col = data.getColumnCount();//gets number of columns
        String column = Integer.toString(col);//converts to int

        Log.d(TAG, column);
        while (data.moveToNext()) {//cursor moves through db
            for(int i=1; i<col; i++) {//iterates through columns retrieving user data
                listData.add(data.getString(i));//adds exercises to the listdata arraylist
            }
            ListAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.my_text_view, listData);//sets data to my_text_view layout
            listView.setAdapter(adapter);
        }
    }
}

