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

import com.developer.calllogmanager.dbHelper.DatabaseHelper;

import java.sql.Date;
import java.time.DayOfWeek;

public class AddReminder extends AppCompatActivity {

    CalendarView calendarView ;
    TimePicker timePicker;
    Button btnSaveReminder , btnDontSaveReminder;
    DatabaseHelper helper;

    int reminderYear , reminderMonth , reminderDayOfMonth , reminderHours , reminderMinutes ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

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

                boolean result = helper.saveReminder(reminderYear , reminderMonth , reminderDayOfMonth , reminderHours , reminderMinutes , "AM");

                if (result) {
                    Snackbar.make(findViewById(android.R.id.content), "Reminder Saved.", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(AddReminder.this, "Reminder Saved", Toast.LENGTH_SHORT).show();

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Error.", Snackbar.LENGTH_LONG).show();
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
