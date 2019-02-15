package com.weller.justlift;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.net.ConnectException;
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

    public static final String Table_5 = "Calorie_Calc";
    public static final String lCol_1 = "WeeklyCalories";
    public static final String lCol_2 = "AddWeeklyWeight";

    public static final String Table_LinearRegression = "LinearRegression";
    public static final String zCol_1 = "WeightChange";
    public static final String zCol_2 = "WeeklyCalories";

    public ProfileDB(Context context ) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table_1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,Surname TEXT,Age INTEGER, Height INTEGER, Weight INTEGER, Gender TEXT, Activity TEXT, Gains TEXT)");
        db.execSQL("create table " + Table_2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,MealName TEXT,Calories INTEGER,Protein INTEGER, Day INTEGER)");
        db.execSQL("create table " + Table_3 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,MealName TEXT,Calories INTEGER,Protein INTEGER, Day INTEGER)");
        db.execSQL("create table " + Table_4 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ExerciseName TEXT)");
        db.execSQL("create table " + Table_5 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,WeeklyCalories DOUBLE, WeeklyWeight DOUBLE)");
        db.execSQL("create table " + ExerciseTable_1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight INTEGER, DAY INTEGER)");
        db.execSQL("create table " + ExerciseTable_2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight INTEGER, DAY INTEGER)");//setting up 5 exercise tables will increase later in development
        db.execSQL("create table " + ExerciseTable_3 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight INTEGER, DAY INTEGER)");
        db.execSQL("create table " + ExerciseTable_4 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight INTEGER, DAY INTEGER)");
        db.execSQL("create table " + ExerciseTable_5 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight INTEGER, DAY INTEGER)");
        db.execSQL("create table " + Table_LinearRegression + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,WeightChange DOUBLE, WeeklyCalories INTEGER)");

    }//executes query put as argument

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_1);
        db.execSQL("DROP TABLE IF EXISTS " + Table_2);
        db.execSQL("DROP TABLE IF EXISTS " + Table_3);
        db.execSQL("DROP TABLE IF EXISTS " + Table_4);
        db.execSQL("DROP TABLE IF EXISTS " + Table_5);
        onCreate(db);
    }


    public boolean addProfileData(String firstName, String surname, String ageString, String heightString, String weightString, String gender, String activity, String gains) {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + Table_1;
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        int icount = cursor.getInt(0);//checks if table has records
        if(icount>0){
            deleteProfileData();//if table has records delete, then begin the add process
        }
        age = Integer.parseInt(ageString);//
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


    public boolean addToWeeklyTable(double weightWeek){
        double caloriesWeek = caloriesWeekly();//gets the weekly calories from method
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(lCol_1, caloriesWeek);
        contentValues.put(lCol_2, weightWeek);//adds weekly calories and weekly weight to the week table

        Log.i(TAG, "addExerciseData: Adding " + caloriesWeek + " " + Table_5);
        long result = db.insert(Table_5, null, contentValues);

        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }


    public boolean addFirstWeeklyTable(double weightWeek){
        double caloriesWeek = caloriesWeekly();//gets the weekly calories from method
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(lCol_1, caloriesWeek);
        contentValues.put(lCol_2, weightWeek);//adds weekly calories and weekly weight to the week table

        Log.i(TAG, "addExerciseData: Adding " + caloriesWeek + " " + Table_5);
        long result = db.insert(Table_5, null, contentValues);

        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getWeeklyTable(){
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT * FROM " + Table_5;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

    public double caloriesWeekly(){
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT " + iCol_2 + " FROM " + Table_3; //gets calories info from past meals table
        Cursor data = db1.rawQuery(query, null);
        double weeklyTotal = 0;//sets weekly value to zero
        while(data.moveToNext()){
            weeklyTotal += data.getInt(0); //adds each meal to the weekly value
        }
        return weeklyTotal;
    }

    public void deletePastMeals(){
        Log.d(TAG, "deleting past meal data");
        SQLiteDatabase db1 = this.getWritableDatabase();
        //db1.execSQL("INSERT INTO " + Table_3 + " (" +  iCol_1 + "," + iCol_2 + "," + iCol_3 + "," + iCol_4 + ") SELECT " + nCol_1 + ","+ nCol_2 + "," + nCol_3 + "," + nCol_4 +" FROM " + Table_2);
        db1.delete(Table_3, null, null);//on method run copies nutrition table to the past meals table, then deletes nutrition table
    }

    public int getDayCount(Context context){
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("dayCount", 0);//0 is private mode
        int dayCount =sp.getInt("dayCount", 1);// gets int stored for day count
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

    public void addFirstLinearRegression(double []l, double[]y){
            double caloriesWeek = caloriesWeekly();//gets the weekly calories from method
            SQLiteDatabase db = this.getWritableDatabase();

            for (int i =0; i<l.length; i++){
                ContentValues cv = new ContentValues();
                cv.put(zCol_1, y[i]);
                cv.put(zCol_2, l[i]);
                db.insert(Table_LinearRegression, null, cv);
            }

      /*  ContentValues contentValues = new ContentValues();
        contentValues.put(lCol_1, caloriesWeek);
        contentValues.put(lCol_2, weightWeek);//adds weekly calories and weekly weight to the week table

        Log.i(TAG, "addExerciseData: Adding " + caloriesWeek + " " + Table_5);
        long result = db.insert(Table_5, null, contentValues);
       */
    }

    public void addLinearRegression(double currentCalories, double weightChange) {
        double caloriesWeek = caloriesWeekly();//gets the weekly calories from method
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(zCol_1, weightChange);
        cv.put(zCol_2, currentCalories);
        db.insert(Table_LinearRegression, null, cv);
    }
    public Cursor getLinearRegression() {
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT * FROM " + Table_LinearRegression;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }
    public Cursor getNutritionData() {
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT * FROM " + Table_2;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

    public Cursor getNutritionProfile(){
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT " + COL_3 + "," + COL_4 + "," + COL_5 + "," + COL_6 + "," + COL_7 + "," + COL_8 + " FROM " + Table_1;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

    public void deleteNutritionData(int dayCount){
        Log.d(TAG, "day count " + dayCount);
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("INSERT INTO " + Table_3 + " (" +  iCol_1 + "," + iCol_2 + "," + iCol_3 + "," + iCol_4 + ") SELECT " + nCol_1 + ","+ nCol_2 + "," + nCol_3 + "," + nCol_4 +" FROM " + Table_2);
        db1.delete(Table_2, null, null);//on method run copies nutrition table to the past meals table, then deletes nutrition table
    }
    public void deleteProfileData(){
        Log.d(TAG, "deleting profile data");
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.delete(Table_1, null, null);//on method run copies nutrition table to the past meals table, then deletes nutrition table
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
    public Cursor getExerciseNames() {
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT * FROM " + Table_4;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

    public Cursor getExerciseData(String tableName) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT * FROM " + tableName;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

    public void recreateNutritionTable(){
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("create table " + Table_2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,MealName TEXT,Calories INTEGER,Protein INTEGER)");
    }



}
