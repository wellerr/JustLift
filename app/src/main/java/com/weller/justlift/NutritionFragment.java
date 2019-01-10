package com.weller.justlift;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class NutritionFragment extends Fragment {

    ProfileDB db;

    TextInputEditText mealEditText;
    TextInputEditText caloriesEditText;
    TextInputEditText proteinEditText;

    Button saveButton;

    String mealString;
    String caloriesString;
    String proteinString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nutrition, container, false);

        mealEditText = v.findViewById(R.id.text_meal);
        caloriesEditText = v.findViewById(R.id.text_calories);
        proteinEditText = v.findViewById(R.id.text_protein);

        saveButton = v.findViewById(R.id.addMealButton);

        db = new ProfileDB(getContext());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealString = mealEditText.getText().toString();
                caloriesString = caloriesEditText.getText().toString();
                proteinString = proteinEditText.getText().toString();

                if (mealString.length() != 0) {
                    db.addMealData(mealString, caloriesString, proteinString);
                }
                else{
                    Toast.makeText(getContext(), "Enter text", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }
}
