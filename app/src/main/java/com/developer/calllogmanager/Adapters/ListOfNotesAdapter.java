package com.developer.calllogmanager.Adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.calllogmanager.CallLogAdapter;
import com.developer.calllogmanager.ListOfNotes;
import com.developer.calllogmanager.Models.ListOfNotesModel;
import com.developer.calllogmanager.Models.SugarModel;
import com.developer.calllogmanager.R;
import com.developer.calllogmanager.databinding.ListRowBinding;
import com.developer.calllogmanager.databinding.RowListOfNotesRecyclerViewBinding;
import com.developer.calllogmanager.voiceupdate.EditNoteActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListOfNotesAdapter extends RecyclerView.Adapter<ListOfNotesAdapter.ListOfNotesViewHolder>{
    RowListOfNotesRecyclerViewBinding itemBinding;
    Context context;
    private ArrayList<ListOfNotesModel> arrayList;
    public ListOfNotesAdapter(ArrayList<ListOfNotesModel> list , Context context ) {
        arrayList = new ArrayList<ListOfNotesModel>();
        for (ListOfNotesModel m: list ) {
            arrayList.add(m);
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
        ListOfNotesModel sugarModel = arrayList.get(position);
        holder.nameTv.setText(sugarModel.getNote());
        holder.statusTv.setText(sugarModel.getStatus());

        itemBinding.editNoteImageViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toEditNote = new Intent(context , EditNoteActivity.class);
                // TODO This activity is being crashed Debug it when you are back to work
                context.startActivity(toEditNote);

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
