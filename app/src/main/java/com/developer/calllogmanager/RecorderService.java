package com.developer.calllogmanager;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class RecorderService extends Service {

    MediaRecorder recorder;
    static final String TAGS=" Inside Service";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent,int flags,int startId)
    {
        recorder = new MediaRecorder();
        recorder.reset();

        String phoneNumber=intent.getStringExtra("number");
        Log.d(TAGS, "Phone number in service: "+phoneNumber);

        Date dateObj = new Date();
        ////////////////////////////////////////////////////////////////
        //Toast.makeText(context, ""+callLog.getDate(), Toast.LENGTH_SHORT).show();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String time=new CallLogUtils(getApplicationContext()).getTIme();

        String path=new CallLogUtils(getApplicationContext()).getPath();
        final int min = 20;
        final int max = 80;
        final int random = new Random().nextInt((max - min) + 1) + min;

        String rec=path+"/"+phoneNumber+"_"+  Calendar.getInstance().getTimeInMillis() +".mp3";
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().putString("filename",rec).apply();
        pref.edit().putString("date",formatter.format(dateObj)).apply();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        recorder.setOutputFile(rec);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();

        Log.d(TAGS, "onStartCommand: "+"Recording started");

        return START_NOT_STICKY;
    }

    public void onDestroy()
    {
        super.onDestroy();

        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder=null;

        Log.d(TAGS, "onDestroy: "+"Recording stopped");

    }
}
