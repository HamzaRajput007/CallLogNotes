package com.developer.calllogmanager.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.calllogmanager.Models.SugarModel;
import com.developer.calllogmanager.R;
import com.developer.calllogmanager.databinding.RowListOfNotesRecyclerViewBinding;
import com.developer.calllogmanager.dbHelper.DatabaseHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListOfNotesAdapter extends RecyclerView.Adapter<ListOfNotesAdapter.ListOfNotesViewHolder>{
    RowListOfNotesRecyclerViewBinding itemBinding;
    Context context;
    private ArrayList<SugarModel> arrayList;
    public ListOfNotesAdapter(ArrayList<SugarModel> list , Context context ) {
        this.context = context;
        arrayList = new ArrayList<SugarModel>();
        for (SugarModel m: list ) {
            String name = m.getNote();
            String number = m.getNumber();
            arrayList.add(m);
            arrayList.indexOf(m);
            int size = arrayList.size();
        }
    }

    @NonNull
    @Override
    public ListOfNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.row_list_of_notes_recycler_view,parent,false);
//        color = context.getResources().getColor(R.color.colorPrimaryDark);
//        filePath = Environment.getExternalStorageDirectory() + "/recorded_audio_notes";
        return new ListOfNotesViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfNotesViewHolder holder, int position) {
        final SugarModel notesModel = arrayList.get(position);
        Long dateValue = Long.parseLong(notesModel.getDate());
        // TODO notesModel is presenting the same data at the view find out why and resolve it when you are back to work
        final Date dateObj = new Date(dateValue);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        itemBinding.textViewNameId.setText(notesModel.getNote());
        itemBinding.textViewStatus.setText(simpleDateFormat.format(dateObj));
        itemBinding.editNoteImageViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                ArrayList<SugarModel> arrayList = new ArrayList<SugarModel>();
                arrayList = dbHelper.FetchData();
                Toast.makeText(context, simpleDateFormat.format(dateObj), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ListOfNotesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv , statusTv;
        View mView ;
        RowListOfNotesRecyclerViewBinding rowListOfNotesRecyclerViewBinding ;
        public ListOfNotesViewHolder(RowListOfNotesRecyclerViewBinding itemView ) {
          /*  super(itemView);
            nameTv = itemView.findViewById(R.id.textViewNameId);
            statusTv = itemView.findViewById(R.id.textViewStatus);
            onNoteClickListenerViewHOlder = onNoteClickListener;
            mView = itemView;*/
//            itemView.setOnClickListener(this);
            super(itemView.getRoot());
            rowListOfNotesRecyclerViewBinding = itemView;



        }


    }

}
