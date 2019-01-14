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

    public NutritionAdapter(Context context, int resource, ArrayList<NutritionData> nutrition) {
        super(context, resource, nutrition);
        this.nutrition = nutrition;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parents){
        convertView = mInflater.inflate(mViewResourceId,null);

        NutritionData nutritiondata = nutrition.get(position);

        if(nutritiondata != null){
            TextView mealText = convertView.findViewById(R.id.textView1);
            TextView caloriesText = convertView.findViewById(R.id.textView2);
            TextView proteinText = convertView.findViewById(R.id.textView3);

            if(mealText != null){
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
