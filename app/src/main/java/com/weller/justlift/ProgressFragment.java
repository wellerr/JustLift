package com.weller.justlift;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.weller.justlift.ProfileDB.TAG;

public class ProgressFragment extends Fragment {

    private ListView listView;
    public ProfileDB db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        db = new ProfileDB(getContext());
        listView = v.findViewById(R.id.listView);
        populateListView();

        return v;
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
