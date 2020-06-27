package com.developer.calllogmanager.dbHelper;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.VisibleForTesting;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


import com.developer.calllogmanager.Models.SugarModel;
import com.orm.dsl.Table;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;
    public static final String DATABASE_NAME = "NewQWikiNotes.db";

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

    public static final String TABLE_VOICE_NOTES = "TABLEVoiceNotes";
    public static final String COL_1_voice_ID = "ID";
    public static final String COL_2_FILENOTESNAME = "FILENOTESNAME";
    public static final String COL_3_VOICENOTETIME = "VOICENOTETIME";

    public static final String TABLE_STATUS = "TABLESTATUS";
    public static final String COL_1_STATUS_ID = "ID";
    public static final String COL_2_STATUS_DATE = "StatusDate";
    public static final String COL_3_STATUS_VALUE = "StatusValue";


    public static final String TABLE_CALLRECORDING = "CallRecording";
    public static final String COL_1_ID = "ID";
    public static final String COL_2_Filename = "FileName";
    public static final String COL_3_CallTime = "CallTime";

    public static final String TABLE_NOTES_OF_CALL_LOG = "NotesOfCallLog";
    public static final String NOTE_ID = "ID";
    public static final String NOTE_CONTENT = "NoteContent";
    public static final String NOTE_TIME_STAMP = "NoteTimeStamp";
    public static final String NOTE_CALL_LOG_ID = "NoteCallLogId";

    public static final String REMINDERS_TABLE  = "REMINDERS_TABLE";
    public static final String REMINDER_ID = "ID";

    public static final String REMINDER_NOTE_ID = "NOTE_ID";

    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);
        sqLiteDatabase = this.getWritableDatabase();
        this.context = context;
//        onCreate(sqLiteDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String query_voice_notes = (" CREATE TABLE IF NOT EXISTS " + TABLE_VOICE_NOTES + " (" +
                COL_1_voice_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT," +
                COL_2_FILENOTESNAME + " VARCHAR," +
                COL_3_VOICENOTETIME + " VARCHAR );"
        );
        
        sqLiteDatabase.execSQL(query_voice_notes);

        /*String query_reminder_table = (" CREATE TABLE IF NOT EXISTS " + REMINDERS_TABLE + " ( " +
              REMINDER_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                HOURS +" INTEGER, " +
                MINUTES +" INTEGER, " +
                DAY_OF_MONTH +" INTEGER, " +
                MONTH +" INTEGER, " +
                YEAR +" INTEGER, " +
                REMINDER_NOTE_ID + "INTEGER," +
                AMPM + "VARCHAR"+");"
        );
        sqLiteDatabase.execSQL(query_reminder_table);

         public static final String COL_1 = "ID";
//    public static final String COL_2 = "DATE";
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

*/
        String query_signup = (" CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER  PRIMARY KEY AUTOINCREMENT," +
                COL_3 + " VARCHAR," +
                COL_2 + "VARCHAR," +
                COL_5 + " VARCHAR," +
                HOURS + "INTEGER," +
                MINUTES + "INTEGER," +
                DAY_OF_MONTH + "INTEGER," +
                MONTH + "INTEGER," +
                YEAR + "INTEGER," +
                AMPM + "VARCHAR," +
                COL_4 + " VARCHAR"+");"
        );
        sqLiteDatabase.execSQL(query_signup);

       /* String query_table_notes_of_call_log = (" CREATE TABLE  " + TABLE_NOTES_OF_CALL_LOG + " (" +
                NOTE_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT," +
                NOTE_CONTENT + " VARCHAR ," +
                NOTE_TIME_STAMP + " TEXT , "+
                NOTE_CALL_LOG_ID +"INTEGER"+
                "FOREIGN KEY "+ NOTE_CALL_LOG_ID + "REFERENCE" + TABLE_NAME + "(" + COL_1 +" ));");
        sqLiteDatabase.execSQL((query_table_notes_of_call_log));*/

        String query_status = (" CREATE TABLE IF NOT EXISTS " + TABLE_STATUS + " (" +
                COL_1_STATUS_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT," +
                COL_2_STATUS_DATE + " VARCHAR," +
                COL_3_STATUS_VALUE + " VARCHAR );"
        );
        sqLiteDatabase.execSQL(query_status);

        String query_customer = (" CREATE TABLE  IF NOT EXISTS " + TABLE_CALLRECORDING + " (" +
                COL_1_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT," +
                COL_2_Filename + " VARCHAR," +
                COL_3_CallTime + " VARCHAR );"
        );
        sqLiteDatabase.execSQL(query_customer);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_VOICE_NOTES);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_CALLRECORDING);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_STATUS);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NOTES_OF_CALL_LOG);
//        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + REMINDERS_TABLE);
        onCreate(sqLiteDatabase);

    }

    /*public Cursor getReminder(int noteID){
        sqLiteDatabase = this.getReadableDatabase();
        String[] columns = {REMINDER_ID , HOURS , MINUTES , YEAR , MONTH , DAY_OF_MONTH};
        String selection = REMINDER_NOTE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(noteID)};
        Cursor cursor = sqLiteDatabase.query(REMINDERS_TABLE , columns , selection , selectionArgs , null , null , null);
        if(cursor.moveToFirst()){
            return cursor;
        }
        return null;
    }*/

    //Todo SQLiteException: no such table: REMINDERS_TABLE occurs when inserting ... Resolve it when you are back

   /* public boolean saveReminder(int year , int month , int dayOfMonth , int hours , int minutes , String amPm ){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(YEAR , year);
        cv.put(MONTH , month);
        cv.put(DAY_OF_MONTH , dayOfMonth);
        cv.put(HOURS , hours);
        cv.put(MINUTES , minutes);
        cv.put(AMPM , amPm);
        long ins = sqLiteDatabase.insert(REMINDERS_TABLE , null , cv);
        if(ins == -1 ){
            return false;
        }
        else{
            return true;
        }
    }*/

   /* public  boolean saveNote(String noteContent , String timeStamp , String callLogId){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_CONTENT , noteContent);
        cv.put(NOTE_TIME_STAMP , timeStamp);
        cv.put(NOTE_CALL_LOG_ID , callLogId);
        long ins = sqLiteDatabase.insert(TABLE_NOTES_OF_CALL_LOG , null , cv);
        if (ins == -1) {
            return false;
        }
        else{
            return true;
        }
    }*/

    //To Get a specific note at onclick listener of list_of_note_activity
    public Cursor getNote(int noteId){
        sqLiteDatabase = this.getReadableDatabase();
        String[] columns = {"NOTE_ID", "NOTE_CONTENT" ,  "NOTE_TIME_STAMP" , "NOTE_CALL_LOG_ID"  };
        String selection = NOTE_ID+" = ?";
        String[] selectionArgs = {String.valueOf(noteId)};
        Cursor cursor = sqLiteDatabase.query(TABLE_NOTES_OF_CALL_LOG, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {

            return cursor;
            //   cursor.moveToNext();
        }
        return null;

    }

    public boolean SaveStatus(String date,String value) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2_STATUS_DATE,date);
        cv.put(COL_3_STATUS_VALUE, value);

        long ins = sqLiteDatabase.insert(TABLE_STATUS, null, cv);
        if (ins == -1) {
            return false;
        } else
            return true;

    }

    public String getStatus(String date) {
        sqLiteDatabase = this.getReadableDatabase();

        String[] columns = {COL_1_STATUS_ID, COL_2_STATUS_DATE,COL_3_STATUS_VALUE};
        String selection = COL_2_STATUS_DATE+" = ?";
        String[] selectionArgs = {date};
        Cursor cursor = sqLiteDatabase.query(TABLE_STATUS, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            String value= String.valueOf(cursor.getString(cursor.getColumnIndex(COL_3_STATUS_VALUE)));

            return value;
            //   cursor.moveToNext();
        }
        return "";
    }


    public boolean update_status(String date,String value) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2_STATUS_DATE,date);
        cv.put(COL_3_STATUS_VALUE,value);

        long ins = sqLiteDatabase.update(TABLE_STATUS, cv,"StatusDate="+date,null);
        if (ins == -1) {
            return false;
        } else
            return true;
    }



    public boolean SaveVoiceNOTE(String fileName,String number) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2_FILENOTESNAME,fileName);
        cv.put(COL_3_VOICENOTETIME, number);


        long ins = sqLiteDatabase.insert(TABLE_VOICE_NOTES, null, cv);
        if (ins == -1) {
            return false;
        } else
            return true;

    }


    public String GETVoiceNOTE(String fileName) {
        sqLiteDatabase = this.getReadableDatabase();

        String[] columns = {COL_1_voice_ID, COL_2_FILENOTESNAME,COL_3_VOICENOTETIME};
        String selection = COL_2_FILENOTESNAME+" = ?";
        String[] selectionArgs = {fileName};
        Cursor cursor = sqLiteDatabase.query(TABLE_VOICE_NOTES, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            String file= String.valueOf(cursor.getString(cursor.getColumnIndex(COL_2_FILENOTESNAME)));

            return file;
            //   cursor.moveToNext();
        }
        return "";
    }
    public boolean GETTextNOTE(String fileName) {
        sqLiteDatabase = this.getReadableDatabase();

        String[] columns = {COL_1, COL_2,COL_3,COL_4,COL_5};
        String selection = COL_2+" = ?";
        String[] selectionArgs = {fileName};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            String file= String.valueOf(cursor.getString(cursor.getColumnIndex(COL_2)));

            return true;
            //   cursor.moveToNext();
        }
        return false;
    }

    public boolean SAVENOTE(SugarModel model) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2, model.getDate());
        cv.put(COL_3, model.getNote());
        cv.put(COL_4, model.getExtra());
        cv.put(COL_5, model.getNumber());
        cv.put(HOURS , model.getHours());
        cv.put(MINUTES , model.getMinutes());
        cv.put(DAY_OF_MONTH , model.getDayOfMonth());
//        cv.put(MONTH , model.getMonth());
        cv.put(YEAR , model.getYear());
// todo table callernotes has no column named DATE {Solve it}
        long ins = sqLiteDatabase.insert(TABLE_NAME, null, cv);
        if (ins == -1) {
            return false;
        } else
            return true;
    }
    public Cursor GETNOTE(String date) {
        sqLiteDatabase = this.getReadableDatabase();
        SugarModel model = new SugarModel();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + TABLE_NAME+" WHERE "+COL_2+"="+date, null);
//        sqLiteDatabase.close();
        return cursor;
    }
public ArrayList<SugarModel> FetchData() {
        sqLiteDatabase = this.getReadableDatabase();
        ArrayList <SugarModel> arrayList=new ArrayList<SugarModel>();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + TABLE_NAME,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                SugarModel model = new SugarModel();
                model.setDate(String.valueOf(cursor.getString(cursor.getColumnIndex(COL_2))));
                model.setNote(String.valueOf(cursor.getString(cursor.getColumnIndex(COL_3))));
                model.setExtra(String.valueOf(cursor.getString(cursor.getColumnIndex(COL_4))));
                model.setNumber(String.valueOf(cursor.getString(cursor.getColumnIndex(COL_5))));
                arrayList.add(model);
                cursor.moveToNext();
            }
        }
        return arrayList;
    }

public ArrayList<SugarModel> FetchVoiceData() {
        sqLiteDatabase = this.getReadableDatabase();
        ArrayList <SugarModel> arrayList=new ArrayList<SugarModel>();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + TABLE_VOICE_NOTES,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                SugarModel model = new SugarModel();
                model.setDate(String.valueOf(cursor.getString(cursor.getColumnIndex(COL_2_FILENOTESNAME))));
                model.setNumber(String.valueOf(cursor.getString(cursor.getColumnIndex(COL_3_VOICENOTETIME))));
                arrayList.add(model);
                cursor.moveToNext();
            }
        }
        return arrayList;
    }
    public String select_File(String time) {
        sqLiteDatabase = this.getReadableDatabase();

        String[] columns = {COL_1_ID, COL_2_Filename,COL_3_CallTime};
        String selection = COL_3_CallTime+" = ?";
        String[] selectionArgs = {time};
        Cursor cursor = sqLiteDatabase.query(TABLE_CALLRECORDING, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
           time= String.valueOf(cursor.getString(cursor.getColumnIndex(COL_2_Filename)));

            return time;
         //   cursor.moveToNext();
        }
        return "";
    }

    /*public int delete_Entry(String ID) {
        String where="ID=?";
        sqLiteDatabase=this.getReadableDatabase();
        int numberOFEntriesDeleted= sqLiteDatabase.delete(TABLE_CUS_NAME, where, new String[]{ID}) ;


        return numberOFEntriesDeleted;
    }*/
    public int deleteEntry(String row) {
        sqLiteDatabase = this.getReadableDatabase();
        int var = sqLiteDatabase.delete(TABLE_NAME, COL_2 + "=" + row, null);
        if (var > 0) {
            return var;
        }
        return -1;
    }
    public int deleteVoiceEntry(String row) {
        sqLiteDatabase = this.getReadableDatabase();
        int var = sqLiteDatabase.delete(TABLE_VOICE_NOTES, COL_2_FILENOTESNAME + "=" + row, null);
        if (var > 0) {
            return var;
        }
        return -1;
    }
    public int deletestatus(String row) {
        sqLiteDatabase = this.getReadableDatabase();
        int var = sqLiteDatabase.delete(TABLE_STATUS, COL_2_STATUS_DATE + "=" + row, null);
        if (var > 0) {
            return var;
        }
        return -1;
    }

    public boolean insertfile(String name,String time){

        sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2_Filename,name);
        cv.put(COL_3_CallTime, time);


        long ins = sqLiteDatabase.insert(TABLE_CALLRECORDING, null, cv);
        if (ins == -1) {
            return false;
        } else
            return true;

    }

    public ArrayList<SugarModel> FetchAllStatus() {
        sqLiteDatabase = this.getReadableDatabase();
        ArrayList <SugarModel> arrayList=new ArrayList<SugarModel>();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + TABLE_STATUS,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                SugarModel model = new SugarModel();
                model.setDate(String.valueOf(cursor.getString(cursor.getColumnIndex(COL_2_STATUS_DATE))));
                model.setNote(String.valueOf(cursor.getString(cursor.getColumnIndex(COL_3_STATUS_VALUE))));
                arrayList.add(model);
                cursor.moveToNext();
            }
        }
        return arrayList;
    }


}
