package com.developer.calllogmanager.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.calllogmanager.Models.SugarModel;
import com.developer.calllogmanager.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListOfNotesAdapter extends RecyclerView.Adapter<ListOfNotesAdapter.ListOfNotesViewHolder>{

    Context context;
    private ArrayList<SugarModel> arrayList;

    public ListOfNotesAdapter(ArrayList<SugarModel> list , Context context) {
        this.context = context;
        arrayList = list;
    }

    @NonNull
    @Override
    public ListOfNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListOfNotesViewHolder(LayoutInflater.from(context).inflate(R.layout.list_row,parent,false));    }

    @Override
    public void onBindViewHolder(@NonNull ListOfNotesViewHolder holder, int position) {
        SugarModel sugarModel = arrayList.get(position);
        holder.nameTv.setText(sugarModel.getNote());
        holder.statusTv.setText(sugarModel.getId().toString());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ListOfNotesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv , statusTv;

        public ListOfNotesViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.textViewNameId);
            statusTv = itemView.findViewById(R.id.textViewStatus);
        }
    }
}
