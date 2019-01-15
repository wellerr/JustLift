package com.weller.justlift;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.weller.justlift.ProfileDB.TAG;

public class oldNutritionFragment extends Fragment {

    NutritionData nutrition;
    boolean test;
    String tableName = "Nutrition_table";
    public ProfileDB db;

    TextInputEditText mealEditText;
    TextInputEditText caloriesEditText;
    TextInputEditText proteinEditText;

    Button saveButton;

    String mealString;
    String caloriesString;
    String proteinString;
    private ListView listView;

    ArrayList<NutritionData> listData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.old_fragment_nutrition, container, false);

        mealEditText = v.findViewById(R.id.text_meal);
        caloriesEditText = v.findViewById(R.id.text_calories);
        proteinEditText = v.findViewById(R.id.text_protein);

        saveButton = v.findViewById(R.id.addMealButton);

        db = new ProfileDB(getContext());
        listView = v.findViewById(R.id.listView);
        populateListView();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealString = mealEditText.getText().toString();
                caloriesString = caloriesEditText.getText().toString();
                proteinString = proteinEditText.getText().toString();

                if (mealString.length() != 0) {
                    test = db.addMealData(mealString, caloriesString, proteinString);
                    populateListView();
                    if(test)
                    {
                        toastMessage("succesful");
                    }
                    else{
                        toastMessage("failed");
                    }

                }
                else{
                    Toast.makeText(getContext(), "Enter text", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
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
}
