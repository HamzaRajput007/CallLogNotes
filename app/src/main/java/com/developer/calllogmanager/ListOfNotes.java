package com.developer.calllogmanager;

import android.graphics.HardwareRenderer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cleveroad.audiovisualization.DbmHandler;
import com.developer.calllogmanager.Adapters.ListOfNotesAdapter;
import com.developer.calllogmanager.Models.SugarModel;

import java.util.ArrayList;

public class ListOfNotes extends AppCompatActivity {

RecyclerView recyclerViewListOfNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Todo Don't know why this acitivity is not being maped ... map this activity
        setContentView(R.layout.activity_list_of_notes);
        ArrayList<SugarModel> listOfSugarModel = new ArrayList<>();
        SugarModel model = new SugarModel();
        model.setNote("This is the very first note added by hamza");
        model.setExtra("Dead");

        SugarModel model2 = new SugarModel();
        model.setNote("This is the very second note added by hamza");
        model.setExtra("High Priority");
        listOfSugarModel.add(model);
        listOfSugarModel.add(model2);


        recyclerViewListOfNotes = findViewById(R.id.listOfNotesRecyclerView);

        ListOfNotesAdapter adapter = new ListOfNotesAdapter(listOfSugarModel , this);
        recyclerViewListOfNotes.setAdapter(adapter);
    }
}
