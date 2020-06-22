package com.developer.calllogmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.calllogmanager.Models.SugarModel;
import com.developer.calllogmanager.dbHelper.DatabaseHelper;
import com.developer.calllogmanager.voiceupdate.EditNoteActivity;

import java.io.File;
import java.util.ArrayList;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

public class AfterCallActivity extends AppCompatActivity {
    SugarModel model;
    DatabaseHelper databaseHelper;
    ArrayList<CallLogInfo>mainList;
    String filename;
    String date;
    String filePath;
    int color ;
    int requestCode = 0;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().hide();
        setContentView(R.layout.activity_after_call);

        model = new SugarModel();
        databaseHelper= new DatabaseHelper(getApplicationContext());
        color = getResources().getColor(R.color.colorPrimaryDark);
         file=new File(Environment.getExternalStorageDirectory()+"/Recorded audio notes/");
        if(!file.exists())
        {
            file.mkdir();
        }

        filePath=file.getAbsolutePath();
        mainList = new ArrayList<>();
        mainList =  loadAllData();

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Write a Note for this call");
        alertBuilder.setMessage("Would you like to add a note and reminder for the last call ?");
        alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToEditNoteActivity();
            }
        });

        alertBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AfterCallActivity.this, "No Notes Added for the call", Toast.LENGTH_SHORT).show();
            }
        });

        alertBuilder.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                showDialog();
//                goToEditNoteActivity();



            }
        },1000);
    }

    private void goToEditNoteActivity() {
        Intent intent=new Intent(AfterCallActivity.this, EditNoteActivity.class);
        intent.putExtra("NUMBER",mainList.get(0).getNumber());
        intent.putExtra("DATE",String.valueOf(mainList.get(0).getDate()));
        startActivity(intent);
    }

    public void showDialog(){
        //////////////////////////////////////
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        filename= pref.getString("filename","");
        date= pref.getString("date","");
        /////////////////////////////////////////
        mainList = new ArrayList<>();
        mainList =  loadAllData();
//        SugarModel fetch = null;
        date= String.valueOf(mainList.get(0).getDate());

        final Dialog selectDialog=new Dialog(this);
        selectDialog.setContentView(R.layout.select_type_dialog);
        selectDialog.setTitle("Note");
        selectDialog.setCancelable(false);
        selectDialog.show();
        TextView cancel,name,number;
        ImageView text,mic;
        cancel   =   selectDialog.findViewById(R.id.cancel);
        name   =   selectDialog.findViewById(R.id.name);
        number   =   selectDialog.findViewById(R.id.number);
        text   =   selectDialog.findViewById(R.id.text);
        mic   =   selectDialog.findViewById(R.id.mic);
        number.setText(mainList.get(0).getNumber());
        name.setText(mainList.get(0).getName());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();
                finishAffinity();
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordVoiceNote();

            }
        });
    }

    public void recordVoiceNote() {
        filename= date;
        filePath=filePath+"/"+ filename +".wav";
        AndroidAudioRecorder.with(AfterCallActivity.this)
                // Required

                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(requestCode)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(true)
                .setKeepDisplayOn(true)

                // Start recording
                .record();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "ye chez", Toast.LENGTH_SHORT).show();
                // Great! User has recorded and saved the audio file
                boolean reault = databaseHelper.SaveVoiceNOTE(filename, String.valueOf(mainList.get(0).getDate()));
                if (reault){
                    Toast.makeText(AfterCallActivity.this, "Succrss", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AfterCallActivity.this, "unsuccessfull", Toast.LENGTH_SHORT).show();

                }
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
                Toast.makeText(getApplicationContext(), "galat chez", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showEditDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.addnote_dialog);
        dialog.setTitle("Note");
        dialog.setCancelable(false);
        TextView save,cancle,delete,txtname,txtnumber;
        save =   dialog.findViewById(R.id.save);
        cancle   =   dialog.findViewById(R.id.cancel);
        delete   =   dialog.findViewById(R.id.delete);
        txtname   =   dialog.findViewById(R.id.name);
        txtnumber   =   dialog.findViewById(R.id.number);
        final EditText edit=(EditText)dialog.findViewById(R.id.grouptitle);

        txtname.setText(mainList.get(0).getNumber());
        txtnumber.setText(mainList.get(0).getName());
        DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
        final ArrayList<CallLogInfo> finalMainList = mainList;
        boolean resultfile= helper.insertfile(filename, String.valueOf(finalMainList.get(0).getDate()));
        if(resultfile){
            //Toast.makeText(AfterCallActivity.this, ""+filename+"\n"+date, Toast.LENGTH_SHORT).show();
        }
        else{
            // Toast.makeText(AfterCallActivity.this, "Not inserted", Toast.LENGTH_SHORT).show();
        }

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text=edit.getText().toString();
                model.setDate(String.valueOf(finalMainList.get(0).getDate()));
                model.setNumber(finalMainList.get(0).getNumber());
                model.setExtra(finalMainList.get(0).getCallType());
                model.setNote(text);




                /*if (edit.getText().toString().isEmpty()){
                    return;
                }
                boolean reault = databaseHelper.SAVENOTE(model);
                if (reault){
                    finishAffinity();
                    dialog.dismiss();
                }*/
                //model.save();
                //List<SugarModel> s = SugarModel.findWithQuery(SugarModel.class,"SELECT * FROM SugarModel WHERE `date`="+mydate);
                ///t.makeText(getActivity().getApplicationContext(), ""+s, Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
                //name=text;

            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String filename= pref.getString("filename","");
                String date= pref.getString("date","");
                DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
                finishAffinity();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = databaseHelper.deleteEntry("");
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

    private ArrayList<CallLogInfo> loadAllData(){
        ArrayList<CallLogInfo>mainList = new ArrayList<>();

        String projection[] = {"_id", CallLog.Calls.NUMBER,CallLog.Calls.DATE,CallLog.Calls.DURATION,CallLog.Calls.TYPE,CallLog.Calls.CACHED_NAME};
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        @SuppressLint("MissingPermission") Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI,projection,null,null,CallLog.Calls.DEFAULT_SORT_ORDER);

        if(cursor == null){
            Log.d("CALLLOG","cursor is null");
            return null;
        }

        if(cursor.getCount() == 0){
            Log.d("CALLLOG","cursor size is 0");
            return null;
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


            cursor.moveToNext();
        }
        cursor.close();
        return mainList;
    }

}
