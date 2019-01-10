package com.weller.justlift;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

import static java.security.AccessController.getContext;

public class ProfileDB extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelp";

    public static final String DB_Name = "User.db";
    public static final String Table_Name = "Profile_table";
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

    public ProfileDB(Context context ) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table_Name + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,Surname TEXT,Age INTEGER, Height INTEGER, Weight INTEGER, Gender TEXT, Activity TEXT, Gains TEXT)");
    }//executes query put as argument



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }


    public boolean addData (String firstName, String surname, String ageString, String heightString, String weightString, String gender, String activity, String gains) {
        SQLiteDatabase db = this.getWritableDatabase();
        //SQLiteDatabase db;

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

        Log.i(TAG, "addData: Adding " + firstName + " " + surname + " " + age + " " + height +   " to " + Table_Name);
        long result = db.insert(Table_Name, null, contentValues);

       /* if (firstName.contains("a")){
            result=0;
        }
        */
        if (result == -1){
            return false;
        }
        else{
            return true;
        }


    }
    public Cursor getData() {
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT * FROM " + Table_Name;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }

}
