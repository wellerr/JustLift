package com.weller.justlift;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProfileDB extends SQLiteOpenHelper {

    /*
    * This class is used to setup the database used by the app and make specific adjustments to tables to provide intended functionality
    * */

    private static final String TAG = "DatabaseHelp";

    private static final String DB_Name = "User.db";

    static final String Table_1 = "Profile_table";//table name - this table is used to save the profile information of the user
    private static final String COL_1 = "FirstName";
    private static final String COL_2 = "Surname";
    private static final String COL_3 = "Age";
    private static final String COL_4 = "Height";
    private static final String COL_5 = "Weight";
    private static final String COL_6 = "Gender";
    private static final String COL_7 = "Activity";
    private static final String COL_8 = "Gains";//column names

    final String Table_2 = "Nutrition_table";//table name - this saves the meals for the current day for the user
    private static final String nCol_1 = "MealName";
    private static final String nCol_2 = "Calories";
    private static final String nCol_3 = "Protein";
    private static final String nCol_4 = "Day";//column names

    static final String Table_3 = "Past_meals";//table name - this has the nutrition table meals copied into it once user completes day
    private static final String iCol_1 = "MealName";
    private static final String iCol_2 = "Calories";
    private static final String iCol_3 = "Protein";
    private static final String iCol_4 = "Day";//column names

    static final String Table_4 = "Exercise_Table";//table name - this table stores the user specified exercise names for display in app
    private static final String jCol_1 = "ExerciseName";//column name

    static final String Table_5 = "Calorie_Calc";//table name - used to calculate weekly calorie count and the updated user weight
    private static final String lCol_1 = "WeeklyCalories";
    private static final String lCol_2 = "AddWeeklyWeight";//column names

    static final String Table_LinearRegression = "LinearRegression";//table name - used for training data to calculate needed calories
    private static final String zCol_1 = "WeightChange";
    private static final String zCol_2 = "WeeklyCalories";//column names

    static final String ExerciseTable_1 = "Exercise_Table1";//table name/s - these tables are used to track 5 user exercises when they update weight lifted each workout
    static final String ExerciseTable_2 = "Exercise_Table2";
    static final String ExerciseTable_3 = "Exercise_Table3";
    static final String ExerciseTable_4 = "Exercise_Table4";
    static final String ExerciseTable_5 = "Exercise_Table5";
    private static final String kCol_1 = "Weight";
    private static final String kCol_2 = "Day ";//column names

    protected ProfileDB(Context context ) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//on class creation, all tables are created
        db.execSQL("create table " + Table_1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,Surname TEXT,Age INTEGER, Height DOUBLE, Weight DOUBLE, Gender TEXT, Activity TEXT, Gains TEXT)");
        db.execSQL("create table " + Table_2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,MealName TEXT,Calories INTEGER,Protein INTEGER, Day INTEGER)");
        db.execSQL("create table " + Table_3 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,MealName TEXT,Calories INTEGER,Protein INTEGER, Day INTEGER)");
        db.execSQL("create table " + Table_4 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ExerciseName TEXT)");
        db.execSQL("create table " + Table_5 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,WeeklyCalories DOUBLE, AddWeeklyWeight DOUBLE)");
        db.execSQL("create table " + ExerciseTable_1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight DOUBLE, DAY INTEGER)");
        db.execSQL("create table " + ExerciseTable_2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight DOUBLE, DAY INTEGER)");
        db.execSQL("create table " + ExerciseTable_3 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight DOUBLE, DAY INTEGER)");
        db.execSQL("create table " + ExerciseTable_4 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight DOUBLE, DAY INTEGER)");
        db.execSQL("create table " + ExerciseTable_5 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Weight DOUBLE, DAY INTEGER)");
        db.execSQL("create table " + Table_LinearRegression + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,WeightChange DOUBLE, WeeklyCalories INTEGER)");

    }//executes query put as argument

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//when app version updates all tables are dropped
        db.execSQL("DROP TABLE IF EXISTS " + Table_1);
        db.execSQL("DROP TABLE IF EXISTS " + Table_2);
        db.execSQL("DROP TABLE IF EXISTS " + Table_3);
        db.execSQL("DROP TABLE IF EXISTS " + Table_4);
        db.execSQL("DROP TABLE IF EXISTS " + Table_5);
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseTable_1);
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseTable_2);
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseTable_3);
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseTable_4);
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseTable_5);
        db.execSQL("DROP TABLE IF EXISTS " + Table_LinearRegression);
        onCreate(db);
    }

    private boolean truefalse(long result){//this method is used by add methods to return true/false if add has been successful
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    boolean addProfileData(String firstName, String surname, String ageString, String heightString, String weightString, String gender, String activity, String gains) {
        //This method is responsible for adding profile info to the table
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + Table_1;//checks if there is data in table
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        int icount = cursor.getInt(0);//checks if table has records
        if(icount>0){//if there is data, its deleted
            deleteProfileData();//if table has records delete, then begin the add process
        }
        int age = Integer.parseInt(ageString);
        double height = Double.parseDouble(heightString);//Change string values into int before putting into db
        double weight = Double.parseDouble(weightString);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, firstName);
        contentValues.put(COL_2, surname);
        contentValues.put(COL_3, age);
        contentValues.put(COL_4, height);
        contentValues.put(COL_5, weight);
        contentValues.put(COL_6, gender);
        contentValues.put(COL_7, activity);
        contentValues.put(COL_8, gains);//adds all the data to contentvalues

        Log.i(TAG, "addProfileData: Adding " + firstName + " " + surname + " " + age + " " + height +   " to " + Table_1);//log for testing
        long result = db.insert(Table_1, null, contentValues); //inserts profile data, and returns -1 if fails
        cursor.close();
        return truefalse(result);
    }

    boolean addMealData(String meal, String caloriesString, String proteinString, int dayCount){
        SQLiteDatabase db = this.getWritableDatabase();
        int calories = Integer.parseInt(caloriesString);
        int protein = Integer.parseInt(proteinString);
        ContentValues contentValues = new ContentValues();
        contentValues.put(nCol_1, meal);
        contentValues.put(nCol_2, calories);
        contentValues.put(nCol_3, protein);
        contentValues.put(nCol_4, dayCount);//adds the meal input to the table
        Log.i(TAG, "addProfileData: Adding " + meal + " " + caloriesString + " " + proteinString  + " " + dayCount + " to " + Table_2);//log for testing
        long result = db.insert(Table_2, null, contentValues);//inserts meals into table
        return truefalse(result);
    }
    boolean addExerciseData(String exercise){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(jCol_1, exercise);
        Log.i(TAG, "addExerciseData: Adding " + exercise + " " + Table_4);
        long result = db.insert(Table_4, null, contentValues);
        return truefalse(result);
    }
    boolean addExerciseWeight(String tableName, int exerciseWeight, int dayCount){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(kCol_1, exerciseWeight);
        contentValues.put(kCol_2, dayCount);

        Log.i(TAG, "addExerciseData: Adding " + exerciseWeight + " " + tableName);
        long result = db.insert(tableName, null, contentValues);
        return truefalse(result);
    }

    boolean addToWeeklyTable(double weightWeek){
        double caloriesWeek = caloriesWeekly();//gets the weekly calories from method
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(lCol_1, caloriesWeek);
        contentValues.put(lCol_2, weightWeek);//adds weekly calories and weekly weight to the week table

        Log.i(TAG, "addExerciseData: Adding " + caloriesWeek + " " + Table_5);
        long result = db.insert(Table_5, null, contentValues);
        return truefalse(result);
    }

    private double caloriesWeekly(){//gets the total calories for the week
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT " + iCol_2 + " FROM " + Table_3; //gets calories info from past meals table
        Cursor data = db1.rawQuery(query, null);
        double weeklyTotal = 0;//sets weekly value to zero
        while(data.moveToNext()){
            weeklyTotal += data.getInt(0); //adds each meal to the weekly value
        }
        data.close();
        return weeklyTotal;//returns weekly total
    }

    void deletePastMeals(){//clears the past meals table
        Log.d(TAG, "deleting past meal data");
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.delete(Table_3, null, null);//on method run copies nutrition table to the past meals table, then deletes nutrition table
    }

    int getDayCount(Context context){//gets the day count from shared prefs
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("dayCount", 0);//0 is private mode (only accessed by this app)
        int dayCount =sp.getInt("dayCount", 1);// gets int stored for day count
        return dayCount;
    }

    void updateDayCount(Context context, int count){//updates the day count
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("dayCount", 0);//0 is private mode
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("dayCount", count);
        editor.apply();
    }

    void addFirstLinearRegression(double []l, double[]y){//this is ran first time linear regression occurs
            double caloriesWeek = caloriesWeekly();//gets the weekly calories from method
            SQLiteDatabase db = this.getWritableDatabase();
            for (int i =0; i<l.length; i++){
                ContentValues cv = new ContentValues();
                cv.put(zCol_1, y[i]);//puts first training data into the content values
                cv.put(zCol_2, l[i]);
                db.insert(Table_LinearRegression, null, cv);//adds content values to linear regression table
            }
    }
    void addLinearRegression(double currentCalories, double weightChange) {//after linear regression has occured once this is ran next times
        //training data already available
        double caloriesWeek = caloriesWeekly();//gets the weekly calories from method
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(zCol_1, weightChange);
        cv.put(zCol_2, currentCalories);
        db.insert(Table_LinearRegression, null, cv);//adds the newest weeks data to the training data
    }
    Cursor getLinearRegression() {//used to get the linear regression table
        getTable(Table_LinearRegression);
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT * FROM " + Table_LinearRegression;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

    Cursor getTable(String tableName){//this is going to return a cursor of a full table of 'tableName'
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT * FROM " + tableName;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

    Cursor getNutritionProfile(){//used to get data from profile table that is used to work out BMR
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT " + COL_3 + "," + COL_4 + "," + COL_5 + "," + COL_6 + "," + COL_7 + "," + COL_8 + " FROM " + Table_1;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

    void deleteNutritionData(int dayCount){//copies meal table to past meals table and then deletes
        Log.d(TAG, "day count " + dayCount);
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("INSERT INTO " + Table_3 + " (" +  iCol_1 + "," + iCol_2 + "," + iCol_3 + "," + iCol_4 + ") SELECT " + nCol_1 + ","+ nCol_2 + "," + nCol_3 + "," + nCol_4 +" FROM " + Table_2);
        db1.delete(Table_2, null, null);//on method run copies nutrition table to the past meals table, then deletes nutrition table
    }
    private void deleteProfileData(){//deletes the profile data
        Log.d(TAG, "deleting profile data");
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.delete(Table_1, null, null);//on method run copies nutrition table to the past meals table, then deletes nutrition table
    }

    Cursor getCaloriesData(){//gets calories column from meals table
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT " + iCol_2 + " FROM " + Table_2;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

    Cursor getProteinData(){//gets protein column from meals table
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT " + iCol_3 + " FROM " + Table_2;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }
}
