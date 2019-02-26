package com.weller.justlift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NutritionAdapter extends ArrayAdapter<NutritionData> {

    private LayoutInflater mInflater;
    private ArrayList<NutritionData> nutrition;
    private int mViewResourceId;

    //this class is responsible for the 3 column layout on my main fragment, the nutrition fragment
    //splits data into meal name, calories, and protein for nice display in the app
    public NutritionAdapter(Context context, int resource, ArrayList<NutritionData> nutrition) {
        super(context, resource, nutrition);
        this.nutrition = nutrition;//instantiates nutrition arraylist from the method inputs
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parents){
        convertView = mInflater.inflate(mViewResourceId,null);

        NutritionData nutritiondata = nutrition.get(position);

        if(nutritiondata != null){//if the nutrition data isn't empty
            TextView mealText = convertView.findViewById(R.id.textView1);//sets textview1 to equal meal name
            TextView caloriesText = convertView.findViewById(R.id.textView2);//sets textview2 to equal calories
            TextView proteinText = convertView.findViewById(R.id.textView3);//sets textview3 to equal protein

            if(mealText != null){//if doesn't update calls function to get meal name
                mealText.setText((nutritiondata.getMealName()));
            }
            if(caloriesText != null){
                caloriesText.setText((nutritiondata.getCalories()));
            }
           if(proteinText != null){
                proteinText.setText((nutritiondata.getProtein()));
            }
        }
        return convertView;
    }
}
