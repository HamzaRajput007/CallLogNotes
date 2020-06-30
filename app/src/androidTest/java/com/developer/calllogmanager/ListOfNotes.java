package com.developer.calllogmanager;

import android.content.Intent;
import android.database.Cursor;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.developer.calllogmanager.Adapters.ListOfNotesAdapter;
import com.developer.calllogmanager.Models.ListOfNotesModel;
import com.developer.calllogmanager.Models.SugarModel;
import com.developer.calllogmanager.R;
import com.developer.calllogmanager.dbHelper.DatabaseHelper;

import java.util.ArrayList;
import java.util.Locale;


public class ListOfNotes extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;


    DatabaseHelper databaseHelper;

    RecyclerView recyclerViewListOfNotes;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:
            {
                if (resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    editText.setText(result.get(0));




                    if(result.get(0).equals("save note")) {

                        Toast.makeText(this, ""+result, Toast.LENGTH_SHORT).show();

//                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//                        startActivityForResult(intent, 2);
                    }
//                    else {
//                        Toast.makeText(this, " sorry"+result, Toast.LENGTH_SHORT).show();
//
//                    }
                }
                break;
            }
        }
    }

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
