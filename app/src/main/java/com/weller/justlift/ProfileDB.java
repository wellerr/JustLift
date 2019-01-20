package com.weller.justlift;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

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
    public static final String nCol_4 = "Day";


    public static final String Table_3 = "Past_meals";
    public static final String iCol_1 = "MealName";
    public static final String iCol_2 = "Calories";
    public static final String iCol_3 = "Protein";
    public static final String iCol_4 = "Day";

    private int calories;
    private int protein;

    public static final String Table_4 = "Exercise_Table";
    public static final String jCol_1 = "ExerciseName";

    public static final String ExerciseTable_1 = "Exercise_Table1";
    public static final String ExerciseTable_2 = "Exercise_Table2";
    public static final String ExerciseTable_3 = "Exercise_Table3";
    public static final String ExerciseTable_4 = "Exercise_Table4";
    public static final String ExerciseTable_5 = "Exercise_Table5";

    public static final String kCol_1 = "Weight";
    public static final String kCol_2 = "Day ";

    public ProfileDB(Context context ) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table_1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,Surname TEXT,Age INTEGER, Height INTEGER, Weight INTEGER, Gender TEXT, Activity TEXT, Gains TEXT)");
        db.execSQL("create table " + Table_2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,MealName TEXT,Calories INTEGER,Protein INTEGER, Day INTEGER)");
        db.execSQL("create table " + Table_3 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,MealName TEXT,Calories INTEGER,Protein INTEGER, Day INTEGER)");
        db.execSQL("create table " + Table_4 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ExerciseName TEXT)");
        db.execSQL("create table " + ExerciseTable_1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight INTEGER, DAY INTEGER)");
        db.execSQL("create table " + ExerciseTable_2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight INTEGER, DAY INTEGER)");//setting up 5 exercise tables will increase later in development
        db.execSQL("create table " + ExerciseTable_3 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight INTEGER, DAY INTEGER)");
        db.execSQL("create table " + ExerciseTable_4 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight INTEGER, DAY INTEGER)");
        db.execSQL("create table " + ExerciseTable_5 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight INTEGER, DAY INTEGER)");
    }//executes query put as argument

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_1);
        db.execSQL("DROP TABLE IF EXISTS " + Table_2);
        db.execSQL("DROP TABLE IF EXISTS " + Table_3);
        db.execSQL("DROP TABLE IF EXISTS " + Table_4);
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

    public boolean addMealData(String meal, String caloriesString, String proteinString, int dayCount){
        SQLiteDatabase db = this.getWritableDatabase();

        calories = Integer.parseInt(caloriesString);
        protein = Integer.parseInt(proteinString);

        ContentValues contentValues = new ContentValues();
        contentValues.put(nCol_1, meal);
        contentValues.put(nCol_2, calories);
        contentValues.put(nCol_3, protein);
        contentValues.put(nCol_4, dayCount);

        Log.i(TAG, "addProfileData: Adding " + meal + " " + caloriesString + " " + proteinString  + " " + dayCount + " to " + Table_2);
        long result = db.insert(Table_2, null, contentValues);

        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean addExerciseData(String exercise){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(jCol_1, exercise);

        Log.i(TAG, "addExerciseData: Adding " + exercise + " " + Table_4);
        long result = db.insert(Table_4, null, contentValues);

        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean addExerciseWeight(String tableName, int exerciseWeight, int dayCount){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(kCol_1, exerciseWeight);
        contentValues.put(kCol_2, dayCount);

        Log.i(TAG, "addExerciseData: Adding " + exerciseWeight + " " + tableName);
        long result = db.insert(tableName, null, contentValues);

        if (result == -1){
            return false;
        }
        else{
            return true;
        }
            }

    public int getDayCount(Context context){
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("dayCount", 0);//0 is private mode
        SharedPreferences.Editor editor = sp.edit();
        int dayCount =sp.getInt("dayCount", 0);// gets int stored for day count
        return dayCount;
    }

    public void updateDayCount(Context context, int count){
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("dayCount", 0);//0 is private mode
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("dayCount", count);
        editor.commit();
    }

    public Cursor getProfileData() {
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
    public void deleteNutritionData(int dayCount){
        Log.d(TAG, "day count " + dayCount);
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("INSERT INTO " + Table_3 + " (" +  iCol_1 + "," + iCol_2 + "," + iCol_3 + "," + iCol_4 + ") SELECT " + nCol_1 + ","+ nCol_2 + "," + nCol_3 + "," + nCol_4 +" FROM " + Table_2);
        db1.delete(Table_2, null, null);//on method run copies nutrition table to the past meals table, then deletes nutrition table
    }

    public Cursor getCaloriesData(){
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT " + iCol_2 + " FROM " + Table_2;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

    public Cursor getProteinData(){
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT " + iCol_3 + " FROM " + Table_2;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }
    public Cursor getExerciseData() {
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT * FROM " + Table_4;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

    public void recreateNutritionTable(){
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("create table " + Table_2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,MealName TEXT,Calories INTEGER,Protein INTEGER)");
    }

}
