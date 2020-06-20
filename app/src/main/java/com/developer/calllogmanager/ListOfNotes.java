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
import com.developer.calllogmanager.voiceupdate.EditNoteActivity;

import java.util.ArrayList;

public class ListOfNotes extends AppCompatActivity {

RecyclerView recyclerViewListOfNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_notes);
        ArrayList<ListOfNotesModel> listOfSugarModel = new ArrayList<>();
        ListOfNotesModel model = new ListOfNotesModel("Note 1" , "Active" , 1);
        ListOfNotesModel model2 = new ListOfNotesModel("Note 2" , "Dead" , 2);
        listOfSugarModel.add(model);
        listOfSugarModel.add(model2);
        listOfSugarModel.add(model);
        listOfSugarModel.add(model2);
        listOfSugarModel.add(model);
        listOfSugarModel.add(model2);
        listOfSugarModel.add(model);
        listOfSugarModel.add(model2);
        listOfSugarModel.add(model);
        listOfSugarModel.add(model2);
        listOfSugarModel.add(model);
        listOfSugarModel.add(model2);

        recyclerViewListOfNotes = findViewById(R.id.listOfNotesRecyclerView);
        recyclerViewListOfNotes.setHasFixedSize(true);
        recyclerViewListOfNotes.setLayoutManager(new LinearLayoutManager(this));
        ListOfNotesAdapter adapter = new ListOfNotesAdapter(listOfSugarModel , this);
        recyclerViewListOfNotes.setAdapter(adapter);

        Toast.makeText(this, String.valueOf(listOfSugarModel.size()), Toast.LENGTH_SHORT).show();
    }

}
