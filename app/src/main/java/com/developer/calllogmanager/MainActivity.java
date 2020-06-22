package com.developer.calllogmanager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.developer.calllogmanager.Models.SugarModel;
import com.developer.calllogmanager.databinding.ActivityMainBinding;
import com.developer.calllogmanager.dbHelper.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FrameLayout frmelayout;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().putInt("numOfCalls",0).apply();
        initComponents();
        ////////////////
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
        ////////////////
        final String SOME_ACTION = "android.intent.action.PHONE_STATE";
        intentFilter = new IntentFilter(SOME_ACTION);
        if (getIntent().hasExtra("call")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CallLogUtils callLogUtils = CallLogUtils.getInstance(getApplicationContext());
                    ArrayList<CallLogInfo> callLogInfos = new ArrayList<>();
                    callLogInfos =callLogUtils.readCallLogs();
                    showDialog(String.valueOf(callLogInfos.get(0).getDate()),callLogInfos.get(0).getNumber(),callLogInfos.get(0).getName());
                }
            },2000);
        }
        registerReceiver(new AllCallsReceiver(),intentFilter);
    }

    private void initComponents(){
        setSupportActionBar(binding.toolbar);
        if(getRuntimePermission())
            setUpViewPager();
    }

    private void setUpViewPager(){
        AllCallLogFragment fragment1 = new AllCallLogFragment();
        frmelayout = findViewById(R.id.frmelayout);
        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout,fragment1).commit();
    }

    private boolean getRuntimePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_CONTACTS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},123);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 123){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpViewPager();
            }else{
                finish();
            }
        }
    }
    public void showDialog(final String date, final String number, final String name ){
        final SugarModel model = new SugarModel();
        final DatabaseHelper databaseHelper= new DatabaseHelper(ClassStatic.activity);
//        SugarModel fetch = null;

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.addnote_dialog);
        dialog.setTitle("Note");
        dialog.setCancelable(false);
        final TextView save,cancle,delete,txtname,txtnumber;
        save =   dialog.findViewById(R.id.save);
        cancle   =   dialog.findViewById(R.id.cancel);
        delete   =   dialog.findViewById(R.id.delete);
        txtname   =   dialog.findViewById(R.id.name);
        txtnumber   =   dialog.findViewById(R.id.number);
        final EditText edit=(EditText)dialog.findViewById(R.id.grouptitle);
        final CallLogInfo info = getLastCallDetails(getApplicationContext());
        txtname.setText(info.getName());
        txtnumber.setText(info.getNumber());

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text=edit.getText().toString();
               /* CallLogUtils callLogUtils = CallLogUtils.getInstance(getApplicationContext());
                ArrayList<CallLogInfo> callLogInfos = new ArrayList<>();
                callLogInfos =callLogUtils.readCallLogs();*/

                model.setDate(String.valueOf(info.getDate()));
                model.setNumber(info.getNumber());
                model.setExtra(info.getName());
                model.setNote(text);

                if (edit.getText().toString().isEmpty()){
                    return;
                }
              /*  boolean reault = databaseHelper.SAVENOTE(model);
                if (reault){

                }*/
                AllCallLogFragment fragment1 = new AllCallLogFragment();
                frmelayout = findViewById(R.id.frmelayout);
                getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout,fragment1).commit();
                //model.save();
                //List<SugarModel> s = SugarModel.findWithQuery(SugarModel.class,"SELECT * FROM SugarModel WHERE `date`="+mydate);
                ///t.makeText(getActivity().getApplicationContext(), ""+s, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                //name=text;

            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishAffinity();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = databaseHelper.deleteEntry(date);
                dialog.dismiss();
                if (id>0){
                    edit.setText("");
                }
                else{

                }

            }
        });
        dialog.show();
    }
    public CallLogInfo getLastCallDetails(Context context) {

        CallLogInfo callDetails = new CallLogInfo();

        Uri contacts = CallLog.Calls.CONTENT_URI;
        try {

            Cursor managedCursor = context.getContentResolver().query(contacts, null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");

            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int incomingtype = managedCursor.getColumnIndex(String.valueOf(CallLog.Calls.INCOMING_TYPE));

            if(managedCursor.moveToFirst()){ // added line

                  {
                    String callType;
                    String phNumber = managedCursor.getString(number);
//                    String callerName = getContactName(context, phNumber);
                    if(incomingtype == -1){
                        callType = "incoming";
                    }
                    else {
                        callType = "outgoing";
                    }
                    String callDate = managedCursor.getString(date);
                    //String callDayTime = new Date(Long.valueOf(callDate)).toString();

                    String callDuration = managedCursor.getString(duration);

                    callDetails.setNumber("name here please");
                    callDetails.setNumber(phNumber);
                    callDetails.setCallType(callType);
                      Date dateObj = new Date(callDate);
                      long time = dateObj.getTime();
                      callDetails.setDate(time);
                    ///////////////////////////////////////////
                      /*String date2 = "Tue Apr 23 16:08:28 GMT+05:30 2013";
                      @SuppressLint("NewApi") DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                      LocalDateTime localDate = LocalDateTime.parse(callDayTime, formatter);
                      long timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
                      callDetails.setDate(timeInMilliseconds);
                        //////////////////
                      String oldTime = "05.01.2011, 12:45";
                      Date oldDate = (Date) formatter.parse(oldTime);*/
//                      long oldMillis = oldDate.getTime();
                  }
            }
            managedCursor.close();

        } catch (SecurityException e) {
            Log.e("Security Exception", "User denied call log permission");

        }
        getCallDetails();
        return callDetails;

    }
    private void getCallDetails() {

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery( CallLog.Calls.CONTENT_URI,null, null,null, null);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        sb.append( "Call Details :");
        while ( managedCursor.moveToNext() ) {
            String phNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString( duration );
            String dir = null;
            int dircode = Integer.parseInt( callType );
            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
            sb.append("\n----------------------------------");
        }
        managedCursor.close();
        //call.setText(sb);
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerReceiver(new AllCallsReceiver(),intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(new AllCallsReceiver(),intentFilter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}