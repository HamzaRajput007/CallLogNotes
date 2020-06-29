package com.developer.calllogmanager;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.developer.calllogmanager.Models.SugarModel;
import com.developer.calllogmanager.dbHelper.DatabaseHelper;

import java.sql.Date;
import java.time.DayOfWeek;

public class AddReminder extends AppCompatActivity {

    CalendarView calendarView ;
    TimePicker timePicker;
    Button btnSaveReminder , btnDontSaveReminder;
    DatabaseHelper helper;
    SugarModel modelSugar;
    String date,note,number,extra;
    int reminderYear , reminderMonth , reminderDayOfMonth , reminderHours , reminderMinutes ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        modelSugar  = new SugarModel();
//        modelSugar = (SugarModel) getIntent().getSerializableExtra("SugarModel");

        date = getIntent().getStringExtra("Date");
        note = getIntent().getStringExtra("Note");
        number = getIntent().getStringExtra("Number");
        extra = getIntent().getStringExtra("Extra");

        helper = new DatabaseHelper(this );
        calendarView = findViewById(R.id.calendarViewSaveReminderId);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull  CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(AddReminder.this, String.valueOf(dayOfMonth) + " : " + String.valueOf(month) + " : "  +String.valueOf(year)   , Toast.LENGTH_SHORT).show();

                reminderYear = year ;
                reminderMonth = month;
                reminderDayOfMonth = dayOfMonth;

            }
        });
        timePicker = findViewById(R.id.timePickerSaveReminderId);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                Toast.makeText(AddReminder.this, String.valueOf(hourOfDay) + " " + String.valueOf(minute), Toast.LENGTH_SHORT).show();
                reminderHours = hourOfDay ;
                reminderMinutes = minute;
            }
        });
        btnSaveReminder = findViewById(R.id.saveReminder);
        btnSaveReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                modelSugar.setDate(date);
                modelSugar.setNumber(number);
                modelSugar.setNote(note);
                modelSugar.setExtra(extra);
                modelSugar.setHours(reminderHours);
                modelSugar.setMinutes(reminderMinutes);
                modelSugar.setDayOfMonth(reminderDayOfMonth);
                modelSugar.setMonth(reminderMonth);
                modelSugar.setYear(reminderYear);
                modelSugar.setAmpm("AM");

//                boolean result = helper.SAVENOTE(modelSugar);
                boolean result = helper.SAVENOTE(modelSugar);
                if (result) {
                    Snackbar.make(findViewById(android.R.id.content), "Note Saved.", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(AddReminder.this, "Note Saved", Toast.LENGTH_SHORT).show();

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Error Note Not Saved.", Snackbar.LENGTH_LONG).show();
                }
                finish();
            }
        });
        btnDontSaveReminder = findViewById(R.id.noReminder);
        btnDontSaveReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
