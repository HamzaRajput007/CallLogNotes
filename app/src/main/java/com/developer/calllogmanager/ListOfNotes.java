package com.developer.calllogmanager;

import android.content.Intent;
import android.graphics.HardwareRenderer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cleveroad.audiovisualization.DbmHandler;
import com.developer.calllogmanager.Adapters.ListOfNotesAdapter;
import com.developer.calllogmanager.Models.ListOfNotesModel;
import com.developer.calllogmanager.Models.SugarModel;
import com.developer.calllogmanager.dbHelper.DatabaseHelper;
import com.developer.calllogmanager.voiceupdate.EditNoteActivity;

import java.util.ArrayList;

//Todo make CRUD for List of Notes

public class ListOfNotes extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    RecyclerView recyclerViewListOfNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_notes);

        databaseHelper = new DatabaseHelper(getBaseContext());
        ArrayList<ListOfNotesModel> listOfNotesModels = new ArrayList<>();
        ListOfNotesModel model = new ListOfNotesModel("Note 1" , "Active" , 1);
        ListOfNotesModel model2 = new ListOfNotesModel("Note 2" , "Dead" , 2);

        String Date = getIntent().getStringExtra("DATE");
        ArrayList<SugarModel> notes = new ArrayList<>();

        notes = databaseHelper.GETNOTE(Date);
        listOfNotesModels.add(model);
        listOfNotesModels.add(model2);
        listOfNotesModels.add(model);
        listOfNotesModels.add(model2);
        listOfNotesModels.add(model);
        listOfNotesModels.add(model2);
        listOfNotesModels.add(model);
        listOfNotesModels.add(model2);
        listOfNotesModels.add(model);
        listOfNotesModels.add(model2);
        listOfNotesModels.add(model);
        listOfNotesModels.add(model2);

        recyclerViewListOfNotes = findViewById(R.id.listOfNotesRecyclerView);
        recyclerViewListOfNotes.setHasFixedSize(true);
        recyclerViewListOfNotes.setLayoutManager(new LinearLayoutManager(this));
        ListOfNotesAdapter adapter = new ListOfNotesAdapter(notes , this);
        recyclerViewListOfNotes.setAdapter(adapter);

        Toast.makeText(this, Date, Toast.LENGTH_LONG).show();
    }

}
