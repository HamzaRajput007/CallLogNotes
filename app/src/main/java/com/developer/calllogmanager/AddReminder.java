package com.developer.calllogmanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;

public class AddReminder extends AppCompatActivity {

    CalendarView calendarView ;
    TimePicker timePicker;
    Button btnSaveReminder , btnDontSaveReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        calendarView = findViewById(R.id.calendarViewSaveReminderId);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull  CalendarView view, int year, int month, int dayOfMonth) {
                Date date = new Date(year , month , dayOfMonth);
                calendarView.setDate(date.getTime());
            }
        });
        timePicker = findViewById(R.id.timePickerSaveReminderId);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                Toast.makeText(AddReminder.this, String.valueOf(hourOfDay) + " " + String.valueOf(minute), Toast.LENGTH_SHORT).show();
            }
        });
        btnSaveReminder = findViewById(R.id.saveReminder);
        btnDontSaveReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
