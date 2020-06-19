package com.developer.calllogmanager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.CallLog;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class CallLogUtils {

    private static CallLogUtils instance;
    private Context context;
    private ArrayList<CallLogInfo> mainList = null;
    private ArrayList<CallLogInfo> missedCallList = null;
    private ArrayList<CallLogInfo> outgoingCallList = null;
    private ArrayList<CallLogInfo> incomingCallList = null;

    public CallLogUtils(Context context){
        this.context = context;
    }

    public static CallLogUtils getInstance(Context context){
        if(instance == null)
            instance = new CallLogUtils(context);
        return instance;
    }

    private void loadData(){
        mainList = new ArrayList<>();
        missedCallList = new ArrayList<>();
        outgoingCallList = new ArrayList<>();
        incomingCallList = new ArrayList<>();

        String projection[] = {"_id", CallLog.Calls.NUMBER,CallLog.Calls.DATE,CallLog.Calls.DURATION,CallLog.Calls.TYPE,CallLog.Calls.CACHED_NAME};
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI,projection,null,null,CallLog.Calls.DEFAULT_SORT_ORDER);

        if(cursor == null){
            Log.d("CALLLOG","cursor is null");
            return;
        }

        if(cursor.getCount() == 0){
            Log.d("CALLLOG","cursor size is 0");
            return;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            CallLogInfo callLogInfo = new CallLogInfo();
            callLogInfo.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
            callLogInfo.setNumber(cursor.getString(cursor.getColumnIndex( CallLog.Calls.NUMBER )));
            callLogInfo.setCallType(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
            callLogInfo.setDate(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
            callLogInfo.setDuration(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)));
            mainList.add(callLogInfo);

            switch(Integer.parseInt(callLogInfo.getCallType()))
            {
                case CallLog.Calls.OUTGOING_TYPE:
                    outgoingCallList.add(callLogInfo);
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    incomingCallList.add(callLogInfo);
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    missedCallList.add(callLogInfo);
                    break;
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    public ArrayList<CallLogInfo> readCallLogs(){
        if(mainList == null)
            loadData();
        return mainList;
    }

    public ArrayList<CallLogInfo> getMissedCalls(){
        if(mainList == null)
            loadData();
        return missedCallList;
    }

    public ArrayList<CallLogInfo> getIncommingCalls(){
        if(mainList == null)
            loadData();
        return incomingCallList;
    }

    public ArrayList<CallLogInfo> getOutgoingCalls(){
        if(mainList == null)
            loadData();
        return outgoingCallList;
    }

    public long[] getAllCallState(String number){
        long output[] = new long[2];
        for(CallLogInfo callLogInfo : mainList){
            if(callLogInfo.getNumber().equals(number)){
                output[0]++;
                if(Integer.parseInt(callLogInfo.getCallType()) != CallLog.Calls.MISSED_TYPE)
                    output[1]+= callLogInfo.getDuration();
            }
        }
        return output;
    }

    public long[] getIncomingCallState(String number){
        long output[] = new long[2];
        for(CallLogInfo callLogInfo : incomingCallList){
            if(callLogInfo.getNumber().equals(number)){
                output[0]++;
                output[1]+= callLogInfo.getDuration();
            }
        }
        return output;
    }

    public long[] getOutgoingCallState(String number){
        long output[] = new long[2];
        for(CallLogInfo callLogInfo : outgoingCallList){
            if(callLogInfo.getNumber().equals(number)){
                output[0]++;
                output[1]+= callLogInfo.getDuration();
            }
        }
        return output;
    }

    public int getMissedCallState(String number){
        int output =0;
        for(CallLogInfo callLogInfo : missedCallList){
            if(callLogInfo.getNumber().equals(number)){
                output++;
            }
        }
        return output;
    }

    Calendar cal=Calendar.getInstance();
    public String getDateaa()
    {
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH)+1;
        int day=cal.get(Calendar.DATE);
        String date=String.valueOf(day)+"_"+String.valueOf(month)+"_"+String.valueOf(year);
        //setDate(Long.parseLong(date));
        Log.d("TAGCM", "Date "+date);
        return date;
    }
    public String getPath()
    {
        String internalFile= getDateaa();
        File file=new File(Environment.getExternalStorageDirectory()+"/My Records/");
        File file1=new File(Environment.getExternalStorageDirectory()+"/My Records/"+internalFile+"/");
        if(!file.exists())
        {
            file.mkdir();
        }
        if(!file1.exists())
            file1.mkdir();


        String path=file1.getAbsolutePath();
        // Log.d(TAGCM, "Path "+path);

        return path;
    }
    public String getTIme()
    {
        String am_pm="";
        int sec=cal.get(Calendar.SECOND);
        int min=cal.get(Calendar.MINUTE);
        int hr=cal.get(Calendar.HOUR);
        int amPm=cal.get(Calendar.AM_PM);
        if(amPm==1)
            am_pm="PM";
        else if(amPm==0)
            am_pm="AM";

        String time=String.valueOf(hr)+":"+String.valueOf(min)+":"+String.valueOf(sec)+" "+am_pm;

        Log.d("TAGCM", "Date "+time);
        return time;
    }
}