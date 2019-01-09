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
    public static final String COL_1 = "ID";
    public static final String COL_2 = "FirstName";
    public static final String COL_3 = "Surname";
    public static final String COL_4_ = "Age";
    public static final String COL_5 = "Height";

    public ProfileDB(Context context ) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table_Name + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,Surname TEXT,Age INTEGER, Height INTEGER)");
    }//executes query put as argument



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }

    public boolean test(){
        //Log.i(TAG, "successful");
        Random rand = new Random();
        int decide = rand.nextInt(1);
        boolean test;
        if (decide ==1) {
             test = true;
        }
        else{
             test = false;
        }

        return test;
    }

    public boolean addData (String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        //SQLiteDatabase db;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, item);

        Log.i(TAG, "addData: Adding " + item + " to " + Table_Name);
        long result = db.insert(Table_Name, null, contentValues);

//        long result= -1;
        if (item.contains("a")){
            result=0;
        }
        //long result = 0;
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
