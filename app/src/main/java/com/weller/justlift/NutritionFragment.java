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

    ArrayList<NutritionData> listData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nutrition, container, false);
        addMeal = v.findViewById(R.id.addMealButton);
        completeDay = v.findViewById(R.id.completeButton);
        db = new ProfileDB(getContext());
        listView = v.findViewById(R.id.listView);
        populateListView();

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
                toastMessage("complete day selected");

                caloriesList = getColumnData(2);//populate array list with all calories input
                int sumCalories = 0;
                for(int i = 0; i<caloriesList.size(); i++){
//                    sumCalories += Integer.parseInt(caloriesList.get(i));         this needs work
                }
                Log.d(TAG, "summing calories... total calories for today = " + sumCalories);
                //do something with calories sum

                proteinList = getColumnData(3);//populate array list with all protein input
                int sumProtein = 0;
                for(int i = 0; i<caloriesList.size(); i++){
//                    sumProtein += Integer.parseInt(proteinList.get(i));           needs work
                }
                Log.d(TAG, "total protein for today = " + sumProtein);
                //do something with protein sum

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

    public void reloadFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(NutritionFragment.this).attach(NutritionFragment.this).commit();//re-loads the fragment
    }

    public ArrayList<String> getColumnData (int column){

        ArrayList columnData = new ArrayList<Integer>();
        Cursor data = db.getNutritionData();
        int i=0;
        while (data.moveToNext()) {
            columnData.add(data.getString(i));
            i++;

        }
        return columnData;
    }
}


