package com.developer.calllogmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.developer.calllogmanager.Models.SugarModel;
import com.developer.calllogmanager.databinding.ListRowBinding;
import com.developer.calllogmanager.dbHelper.DatabaseHelper;
import com.developer.calllogmanager.voiceupdate.prefrence.SessionManager;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import nl.changer.audiowife.AudioWife;

public class EditNoteActivity extends AppCompatActivity {
    String CallerName;
    String CallerNumber;
    String CallDate;
    DatabaseHelper helper;
    String filename;
    int requestCode = 0;
    String filePath;
    String voicePresent;
    Button btnAddVoice,btnSaveNote;
    TextView header_name,header_number;
    EditText editTextNote;
    Spinner spin;
    String value;
    int mode;
    String chek_status_value;
    TextView tvCurrentDate, tvCurrentTime;
    TimePicker reminderTimePicker ;
    CalendarView reminderDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        helper = new DatabaseHelper(this);
        CallerName = getIntent().getStringExtra("NAME");
        CallerNumber = getIntent().getStringExtra("NUMBER");
        CallDate = getIntent().getStringExtra("DATE");
        filename= CallDate;
        btnSaveNote =   findViewById(R.id.btnSaveNote);
        header_number   =   findViewById(R.id.header_number);
        header_name   =   findViewById(R.id.header_name);
        tvCurrentDate = findViewById(R.id.textViewCallDate);
       /* reminderDatePicker = findViewById(R.id.calendarViewId);
        reminderDatePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(EditNoteActivity.this, String.valueOf(month) + " ", Toast.LENGTH_SHORT).show();
            }
        });*/
      //  btnSaveStatus   =   findViewById(R.id.btnSaveStatus);

        header_name.setText(CallerName);
        header_number.setText(CallerNumber);
        editTextNote=findViewById(R.id.editTextNote);
        spin  =  findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, this.getResources().getStringArray(R.array.select_status));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(adapter);

//        getStatusData(adapter);
        //saveStatusValue();

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String recfilename= pref.getString("filename","");

        final DatabaseHelper helper=new DatabaseHelper(getApplicationContext());

        boolean resultfile= helper.insertfile(recfilename, CallDate);
        if(resultfile){
            //Toast.makeText(AfterCallActivity.this, ""+filename+"\n"+date, Toast.LENGTH_SHORT).show();
        }
        else{
            // Toast.makeText(AfterCallActivity.this, "Not inserted", Toast.LENGTH_SHORT).show();
        }

        File file=new File(Environment.getExternalStorageDirectory()+"/Recorded audio notes/");
        if(!file.exists())
        {
            file.mkdir();
        }

        filePath=file.getAbsolutePath();
        filePath=filePath+"/"+ filename +".wav";
        btnAddVoice=findViewById(R.id.btnAddVoice);
        btnAddVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            recordVoiceNote();
            }
        });
//        getVoiceData();
//        getTextData();
       /* btnSaveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextNote.getText().toString().isEmpty()&&spin.getSelectedItem().toString().equals("Select Status")){
                    Snackbar.make(findViewById(android.R.id.content), "Please select Status or write some Note.", Snackbar.LENGTH_LONG).show();
                }else {
                    saveTextNote();
                    saveStatus();

                    final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(EditNoteActivity.this);
                    alertBuilder.setView(R.layout.ask_reminder_dialog);
                    alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });
                    alertBuilder.show();
                }

            }
        });
    }

    private void saveStatus() {

        if(spin.getSelectedItem().toString().equals("Select Status")){
         return;
        }
            /*if(chek_status_value.equals("Insert")){
                boolean f=  helper.SaveStatus(CallDate, spin.getSelectedItem().toString());
              *//*  if(f)
                    Snackbar.make(findViewById(android.R.id.content), "Status Saved", Snackbar.LENGTH_LONG).show();
              *//*  return;
            }
            if(chek_status_value.equals("Update")) {
                boolean g=helper.update_status(CallDate,spin.getSelectedItem().toString());
               *//* if(g){
                    Snackbar.make(findViewById(android.R.id.content), "Update Saved", Snackbar.LENGTH_LONG).show();
                }*//*
                return;
            }*/
    }

    private void saveTextNote() {
            String text = editTextNote.getText().toString();
            String currentTime=getCurrentTime();
            String currentDate=getCurrentDate();
            SugarModel model = new SugarModel();
            model.setDate(CallDate);
            model.setNumber(CallerNumber);
            model.setExtra(CallerName);
            model.setNote(text);
            model.setCurrentTime(currentTime);
            model.setCurrentDate(currentDate);
            boolean reault = helper.SAVENOTE(model);
            if (reault) {
                Snackbar.make(findViewById(android.R.id.content), "Saved.", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Error.", Snackbar.LENGTH_LONG).show();
            }

    }

    private void getTextData() {
        Cursor fetch = helper.GETNOTE( CallDate);
        if (fetch!=null) {
            tvCurrentDate = findViewById(R.id.header_date);
            tvCurrentTime = findViewById(R.id.header_time);
//            tvCurrentDate.setText(fetch.get(0).getCurrentDate());
//            tvCurrentTime.setText(fetch.get(0).getCurrentTime());
//            if (fetch.get(0).getNote() != null)
//                editTextNote.setText(fetch.get(0).getNote());
        }
    }

    private void getVoiceData() {
        voicePresent=helper.GETVoiceNOTE(CallDate);
        if (voicePresent.length()>4){
            btnAddVoice.setText("Add New Voice Note");
            File file=new File(filePath);
            Uri uri=Uri.fromFile(file);
            ViewGroup mPlayerContainer=(ViewGroup) findViewById(R.id.linear);
            AudioWife.getInstance().init(this,uri).useDefaultUi(mPlayerContainer,getLayoutInflater());
            AudioWife.getInstance().addOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                 //   Toast.makeText(getBaseContext(), "Completed", Toast.LENGTH_SHORT).show();
                    // do you stuff.
                }
            });

        }
        else {
            btnAddVoice.setText("Add Voice Note");
        }
    }
    private void getStatusData(ArrayAdapter<String> adapter) {
        /*String a=helper.getStatus(CallDate);

        if (a.length()>2){
            chek_status_value="Update";
            if (a != null) {
                int spinnerPosition = adapter.getPosition(a);
                spin.setSelection(spinnerPosition);
            }
        }
        else {
            chek_status_value="Insert";
        }*/

    }

    private void recordVoiceNote() {
        filename= CallDate;
                // Required
        int  color = getResources().getColor(R.color.colorPrimaryDark);
        File file=new File(Environment.getExternalStorageDirectory()+"/Recorded audio notes/");
        if(!file.exists())
        {
            file.mkdir();
        }

        String filePath=file.getAbsolutePath();
         filePath=filePath+"/"+ filename +".wav";

        AndroidAudioRecorder.with(EditNoteActivity.this)
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
            //    Toast.makeText(getApplicationContext(), "ye chez", Toast.LENGTH_SHORT).show();
                // Great! User has recorded and saved the audio file
                boolean reault = helper.SaveVoiceNOTE(filename,CallDate);
                if (reault){
                    SessionManager sessionManager=new SessionManager(this);
                    sessionManager.setFlag(true);
                    Snackbar.make(findViewById(android.R.id.content), "Voice Note Saved", Snackbar.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(EditNoteActivity.this, MainActivity.class));
                        }
                    },1000);
                }
                else {
                    Snackbar.make(findViewById(android.R.id.content), "Failed", Snackbar.LENGTH_SHORT).show();


                }
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
                Snackbar.make(findViewById(android.R.id.content), "Error in recording voice notes", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
// when done playing, release the resources
        AudioWife.getInstance().release();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditNoteActivity.this, MainActivity.class));
    }


    public void saveStatusValue(){


        if(mode==0){
            value="no selection";
            Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_SHORT).show();
            return;
        }
         if(mode==1){
            value="Call Back";
            Toast.makeText(getApplicationContext(), "1"+" "+value, Toast.LENGTH_SHORT).show();
             return;
         }
         if(mode==2){
            value="Dead";
            Toast.makeText(getApplicationContext(), "2"+" "+value, Toast.LENGTH_SHORT).show();
             return;
        }
         if(mode==3){
            value="Close";
            Toast.makeText(getApplicationContext(), "3"+" "+value, Toast.LENGTH_SHORT).show();
             return;
        }
         if(mode==4){
            value="High Priority";
            Toast.makeText(getApplicationContext(), "4"+" "+value, Toast.LENGTH_SHORT).show();
             return;
        }
         if(mode==5){
            value="Low Priority";
            Toast.makeText(getApplicationContext(), "5"+" "+value, Toast.LENGTH_SHORT).show();
             return;
        }
    }

    private String getCurrentDate() {
        String date;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        date = dateFormat.format(calendar.getTime());
        return date;
    }

    private String getCurrentTime() {
        String time;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        time = dateFormat.format(calendar.getTime());
        return time;
    }

    public void saveReminder(View view) {
        Intent toSaveReminder = new Intent(EditNoteActivity.this , AddReminder.class);
        startActivity(toSaveReminder);
        finish();
    }

    public void cencelReminder(View view) {
        Toast.makeText(this, "No Reminders Added", Toast.LENGTH_SHORT).show();
        Intent toMain = new Intent(EditNoteActivity.this , MainActivity.class);
        startActivity(toMain);
        finish();
    }
}
