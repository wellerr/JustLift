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

import static com.weller.justlift.ProfileDB.TAG;

public class WorkoutFragment extends Fragment {
    ExerciseData workout;
    public ProfileDB db;

    Button addExercise;

    private ListView listView;
    private ArrayList<String> caloriesList;
    private ArrayList<String> proteinList;

    public int dayCount;

    ArrayList<String> listData = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout, container, false);
        addExercise = v.findViewById(R.id.addExercise);

        db = new ProfileDB(getContext());
        listView = v.findViewById(R.id.listView);

        populateListView();

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int listCount = listView.getCount();
                if (listCount <= 4) { //max number of entries -1 (listview count starts at 0)
                    Intent intent = new Intent(getContext(), com.weller.justlift.AddExercise.class);
                    startActivityForResult(intent, 10001);
                }
                else{
                    Toast.makeText(getContext(), "max number of exercises", Toast.LENGTH_LONG);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(),AddExerciseWeight.class);
                intent.putExtra("pos", position);
                startActivityForResult(intent,10001);
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
    public void reloadFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(WorkoutFragment.this).attach(WorkoutFragment.this).commit();//re-loads the fragment
    }
    public void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the List View.");

        Cursor data = db.getExerciseData();
        ArrayList<String> listData = new ArrayList<>();
        int col = data.getColumnCount();
        String column = Integer.toString(col);

        Log.d(TAG, column);
        while (data.moveToNext()) {//cursor moves through db
            for(int i=1; i<col; i++) {//iterates through columns retrieving user data
                listData.add(data.getString(i));
            }

            ListAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listData);
            listView.setAdapter(adapter);
        }
    }
}

