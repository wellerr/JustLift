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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.weller.justlift.ProfileDB.TAG;

public class NutritionFragment extends Fragment {
    NutritionData nutrition;
    public ProfileDB db;

    Button addMeal;
    Button completeDay;

    private ListView listView;
    private ArrayList<String> caloriesList;
    private ArrayList<String> proteinList;

    public int dayCount;

    TextView setCalories;
    TextView setProtein;
    TextView setRemainingCalories;
    TextView getSetRemainingProtein;

    ArrayList<NutritionData> listData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nutrition, container, false);
        addMeal = v.findViewById(R.id.addMealButton);
        completeDay = v.findViewById(R.id.completeButton);
        db = new ProfileDB(getContext());
        listView = v.findViewById(R.id.listView);
        setCalories = v.findViewById(R.id.setCalories);
        setProtein = v.findViewById(R.id.setProtein);
        populateListView();
        totalCalories();
        totalProtein();


        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), com.weller.justlift.AddMeal.class);
                startActivityForResult(intent, 10001);
            }
        });

        completeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               completeDayFunction();
                reloadFragment();
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

    public void completeDayFunction(){ //code that completes day of nutrition for the user
        dayCount = db.getDayCount(getContext());
        dayCount++;//increments days
        db.updateDayCount(getContext(), dayCount);//updates shared prefs with new amount of days
        db.deleteNutritionData(dayCount);//deletes nutrition data, adds nutrition data to the past meals table with the day count as a column
    }
    private void toastMessage(String message){
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }

    public void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the List View.");

        Cursor data = db.getNutritionData();
        listData = new ArrayList<>();

        int col = data.getColumnCount();
        String column = Integer.toString(col);

        int i=0;
        while (data.moveToNext()) {//cursor moves through db
            nutrition = new NutritionData(data.getString(1), data.getString(2), data.getString(3));//gets string variable at each column
            String dataAdded = "Data added " +  data.getString(1) + " " + data.getString(2) + " " + data.getString(3);
            Log.d(TAG, dataAdded);
            listData.add(i,nutrition);//adds data in logical order
            Log.d(TAG,"List adapter starting...");
            NutritionAdapter adapter = new NutritionAdapter(getContext(), R.layout.adapter_view, listData);//puts 3 variables into adapter
            listView.setAdapter(adapter);//views the new table (on screen this shows up as a new row)
            i++;//increments i so next row is done next
        }
    }
    public void totalCalories(){
        Cursor data = db.getCaloriesData();
        int totalCalories =0;//sets initial total to 0
        while (data.moveToNext()){//iterates through calories data
            totalCalories += data.getInt(0);//gets column 0 (first column) moves to next record and adds to total
        }
        Log.d(TAG, "total calories is ..." + totalCalories);
        setCalories.setText(Integer.toString(totalCalories));//sets textview to the total calculated
    }

    public void totalProtein(){
        Cursor data = db.getProteinData();
        int totalProtein =0;
        while (data.moveToNext()){
            totalProtein += data.getInt(0);//gets column 0 (first column) moves to next record and adds to total
        }
        Log.d(TAG, "total protein is ..." + totalProtein);
        setProtein.setText(Integer.toString(totalProtein));
    }

    public void reloadFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(NutritionFragment.this).attach(NutritionFragment.this).commit();//re-loads the fragment

    }

}


