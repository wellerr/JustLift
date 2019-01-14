package com.weller.justlift;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class ProfileDB extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelp";

    public static final String DB_Name = "User.db";

    public static final String Table_1 = "Profile_table";
    public static final String COL_0 = "ID";
    public static final String COL_1 = "FirstName";
    public static final String COL_2 = "Surname";
    public static final String COL_3 = "Age";
    public static final String COL_4 = "Height";
    public static final String COL_5 = "Weight";
    public static final String COL_6 = "Gender";
    public static final String COL_7 = "Activity";
    public static final String COL_8 = "Gains";

    private int age;
    private int weight;
    private int height;

    public static final String Table_2 = "Nutrition_table";
    public static final String nCol_1 = "MealName";
    public static final String nCol_2 = "Calories";
    public static final String nCol_3 = "Protein";

    private int calories;
    private int protein;

    public ProfileDB(Context context ) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table_1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,Surname TEXT,Age INTEGER, Height INTEGER, Weight INTEGER, Gender TEXT, Activity TEXT, Gains TEXT)");
        db.execSQL("create table " + Table_2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,MealName TEXT,Calories INTEGER,Protein INTEGER)");
    }//executes query put as argument



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_1);
        db.execSQL("DROP TABLE IF EXISTS " + Table_2);
        onCreate(db);
    }


    public boolean addProfileData(String firstName, String surname, String ageString, String heightString, String weightString, String gender, String activity, String gains) {
        SQLiteDatabase db = this.getWritableDatabase();

        age = Integer.parseInt(ageString);
        height = Integer.parseInt(heightString);//Change string values into int before putting into db
        weight = Integer.parseInt(weightString);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, firstName);
        contentValues.put(COL_2, surname);
        contentValues.put(COL_3, age);
        contentValues.put(COL_4, height);
        contentValues.put(COL_5, weight);
        contentValues.put(COL_6, gender);
        contentValues.put(COL_7, activity);
        contentValues.put(COL_8, gains);

        Log.i(TAG, "addProfileData: Adding " + firstName + " " + surname + " " + age + " " + height +   " to " + Table_1);
        long result = db.insert(Table_1, null, contentValues);

        if (result == -1){
            return false;
        }
        else{
            return true;
        }

    }

    public boolean addMealData(String meal, String caloriesString, String proteinString){
        SQLiteDatabase db = this.getWritableDatabase();

        calories = Integer.parseInt(caloriesString);
        protein = Integer.parseInt(proteinString);

        ContentValues contentValues = new ContentValues();
        contentValues.put(nCol_1, meal);
        contentValues.put(nCol_2, calories);
        contentValues.put(nCol_3, protein);

        Log.i(TAG, "addProfileData: Adding " + meal + " " + caloriesString + " " + proteinString  + " to " + Table_2);
        long result = db.insert(Table_2, null, contentValues);

        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public Cursor getActivityData() {
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT * FROM " + Table_1;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

    public Cursor getNutritionData() {
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT * FROM " + Table_2;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

}
