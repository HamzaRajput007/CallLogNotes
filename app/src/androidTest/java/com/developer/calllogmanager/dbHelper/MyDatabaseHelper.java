package com.developer.calllogmanager.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;
    public static final String DATABASE_NAME = "notes_app_db.db";

    public static final String TABLE_NAME = "callernotes";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DATE";
    public static final String COL_3 = "NOTE";
    public static final String COL_4 = "EXTRA";
    public static final String COL_5 = "NUMBER";
    //    public static final String COL_6= "CURRENTDATE";
//    public static final String COL_7 = "CURRENTTIME";
    public static final String HOURS = "HOURS";
    public static final String MINUTES = "MINUTES";
    public static final String DAY_OF_MONTH = "DATE";
    public static final String MONTH = "MONTH";
    public static final String YEAR = "YEAR";
    public static final String AMPM = "AMPM";

    public MyDatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null  , 1);
        sqLiteDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        String query_status = (" CREATE TABLE IF NOT EXISTS " + TABLE_STATUS + " (" +
//                COL_1_STATUS_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT," +
//                COL_2_STATUS_DATE + " VARCHAR," +
//                COL_3_STATUS_VALUE + " VARCHAR );"
//        );
//        sqLiteDatabase.execSQL(query_status);

        String query_caller_notes = "";

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
