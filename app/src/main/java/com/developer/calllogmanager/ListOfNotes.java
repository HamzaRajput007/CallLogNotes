package com.developer.calllogmanager;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.developer.calllogmanager.Adapters.ListOfNotesAdapter;
import com.developer.calllogmanager.Models.ListOfNotesModel;
import com.developer.calllogmanager.Models.SugarModel;
import com.developer.calllogmanager.dbHelper.DatabaseHelper;

import java.util.ArrayList;


public class ListOfNotes extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    RecyclerView recyclerViewListOfNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_notes);

        databaseHelper = new DatabaseHelper(getBaseContext());
        ArrayList<SugarModel> records = new ArrayList<>();
        SugarModel model = new SugarModel();
        String Date = getIntent().getStringExtra("DATE");
        Cursor cursor = databaseHelper.GETNOTE(Date);

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                model.setDate(String.valueOf(cursor.getString(cursor.getColumnIndex("DATE"))));
                model.setNote(String.valueOf(cursor.getString(cursor.getColumnIndex("NOTE"))));
                model.setExtra(String.valueOf(cursor.getString(cursor.getColumnIndex("EXTRA"))));
                model.setNumber(String.valueOf(cursor.getString(cursor.getColumnIndex("NUMBER"))));
                model.setCurrentDate(String.valueOf(cursor.getString(cursor.getColumnIndex("CURRENTDATE"))));
                model.setCurrentTime(String.valueOf(cursor.getString(cursor.getColumnIndex("CURRENTTIME"))));
                cursor.moveToNext();
                records.add(model);
            }
        }


        recyclerViewListOfNotes = findViewById(R.id.listOfNotesRecyclerView);
        recyclerViewListOfNotes.setHasFixedSize(true);
        recyclerViewListOfNotes.setLayoutManager(new LinearLayoutManager(this));
        ListOfNotesAdapter adapter = new ListOfNotesAdapter(records , this);
        recyclerViewListOfNotes.setAdapter(adapter);

        Toast.makeText(this, Date, Toast.LENGTH_LONG).show();
    }

}
