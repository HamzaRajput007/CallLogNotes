package com.developer.calllogmanager.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;

    public static final String REMINDERS_TABLE  = "REMINDERS_TABLE";
    public static final String REMINDER_ID = "ID";
    public static final String HOURS = "HOURS";
    public static final String MINUTES = "MINUTES";
    public static final String DAY_OF_MONTH = "DATE";
    public static final String MONTH = "MONTH";
    public static final String YEAR = "YEAR";
    public static final String REMINDER_NOTE_ID = "NOTE_ID";
    public static final String AMPM = "AMPM";

    Context context;
    public MyDatabaseHelper(Context context){
        super(context , "callLogDB" , null , 8);
        sqLiteDatabase = this.getWritableDatabase();
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String query_reminder_table = (" CREATE TABLE " + REMINDERS_TABLE + " ( " +
                REMINDER_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                HOURS +" INTEGER, " +
                MINUTES +" INTEGER, " +
                DAY_OF_MONTH +" INTEGER, " +
                MONTH +" INTEGER, " +
                YEAR +" INTEGER, " +
                AMPM + "VARCHAR" );
        sqLiteDatabase.execSQL(query_reminder_table);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean saveReminder(int year , int month , int dayOfMonth , int hours , int minutes , String amPm , int note_id){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(YEAR , year);
        cv.put(MONTH , month);
        cv.put(DAY_OF_MONTH , dayOfMonth);
        cv.put(HOURS , hours);
        cv.put(MINUTES , minutes);
        cv.put(AMPM , amPm);
        cv.put(REMINDER_NOTE_ID , note_id);
        long ins = sqLiteDatabase.insert(REMINDERS_TABLE , null , cv);
        if(ins == -1 ){
            return false;
        }
        else{
            return true;
        }
    }
}
