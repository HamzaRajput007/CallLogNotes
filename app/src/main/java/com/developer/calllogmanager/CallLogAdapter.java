package com.developer.calllogmanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.calllogmanager.Models.SugarModel;
import com.developer.calllogmanager.databinding.ListRowBinding;
import com.developer.calllogmanager.dbHelper.DatabaseHelper;
import com.developer.calllogmanager.voiceupdate.EditNoteActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CallLogAdapter  extends RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>{

    ArrayList<CallLogInfo> callLogInfoArrayList;
    Context context;
    Activity activity;
    OnCallLogItemClickListener onItemClickListener;
    ArrayList<SugarModel> arrayList;
    RecyclerView recyclerView;
    int color;
    String filePath;
    int requestCode = 0;
    String filenameSave;
    File file;
    private boolean isPlaying = false;
    private MediaPlayer mPlayer;

    SharedPreferences pref;
    interface OnCallLogItemClickListener {
        void onItemClicked(CallLogInfo callLogInfo);
    }

    public CallLogAdapter(Context context){
        callLogInfoArrayList = new ArrayList<>();
        this.context = context;
        this.activity= (Activity) context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        color = context.getResources().getColor(R.color.colorPrimaryDark);
        file=new File(Environment.getExternalStorageDirectory()+"/Recorded audio notes/");
        if(!file.exists())
        {
            file.mkdir();
        }

        filePath=file.getAbsolutePath();
    }

    public void setOnItemClickListener(OnCallLogItemClickListener listener){
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public CallLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ListRowBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.list_row,parent,false);
        color = context.getResources().getColor(R.color.colorPrimaryDark);
        filePath = Environment.getExternalStorageDirectory() + "/recorded_audio_notes";
        return new CallLogViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CallLogViewHolder holder, int position) {
        holder.bind(callLogInfoArrayList.get(position),position);
//        holder.imageViewProfile = fi

    }

    public void addCallLog(CallLogInfo callLogInfo){
        callLogInfoArrayList.add(callLogInfo);
    }

    public void addAllCallLog(ArrayList<CallLogInfo> list,ArrayList<SugarModel> arrayList,RecyclerView recyclerView){
        callLogInfoArrayList.clear();
        callLogInfoArrayList.addAll(list);
        this.arrayList=arrayList;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return callLogInfoArrayList.size();
    }

    class CallLogViewHolder extends RecyclerView.ViewHolder{
        ListRowBinding itemBinding;
        ImageView imageViewProfile;
        public CallLogViewHolder(ListRowBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bind(final CallLogInfo callLog, final int position){
            switch(Integer.parseInt(callLog.getCallType()))
            {
                case CallLog.Calls.OUTGOING_TYPE:
                    itemBinding.imageViewProfile.setImageResource(R.drawable.ic_outgoing);
                    DrawableCompat.setTint(itemBinding.imageViewProfile.getDrawable(), ContextCompat.getColor(context, R.color.green));
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    itemBinding.imageViewProfile.setImageResource(R.drawable.ic_missed);
                    DrawableCompat.setTint(itemBinding.imageViewProfile.getDrawable(), ContextCompat.getColor(context, R.color.blue));
                    itemBinding.textViewCallDuration.setText(Utils.formatSeconds(callLog.getDuration()));
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    itemBinding.imageViewProfile.setImageResource(R.drawable.ic_missed);
                    DrawableCompat.setTint(itemBinding.imageViewProfile.getDrawable(), ContextCompat.getColor(context, R.color.red));
                    break;
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Clicked on " + callLog.getNumber(), Toast.LENGTH_SHORT).show();
                }
            });
            if(TextUtils.isEmpty(callLog.getName()))
                itemBinding.textViewName.setText(callLog.getNumber());
            else
                itemBinding.textViewName.setText(callLog.getName());
            itemBinding.textViewCallDuration.setText(Utils.formatSeconds(callLog.getDuration()));
            Date dateObj = new Date(callLog.getDate());
            ////////////////////////////////////////////////////////////////
            //Toast.makeText(context, ""+callLog.getDate(), Toast.LENGTH_SHORT).show();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            itemBinding.textViewCallNumber.setText(callLog.getNumber());
            if (callLog.isFlag()){
                itemBinding.imagedeletenote.setVisibility(View.VISIBLE);
                itemBinding.imageeditnote.setVisibility(View.VISIBLE);
                itemBinding.imageaddnote.setVisibility(View.GONE);

            }
            else{
                itemBinding.imageeditnote.setVisibility(View.GONE);
                itemBinding.imagedeletenote.setVisibility(View.GONE);
                itemBinding.imageaddnote.setVisibility(View.VISIBLE);

            }
            itemBinding.textViewCallDate.setText(formatter.format(dateObj));

            itemBinding.textViewStatus.setText(callLog.getStatus());

            itemBinding.imageeditnote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
            //        showDialogsds(String.valueOf(callLog.getDate()),callLog.getNumber(),callLog.getName(),itemBinding,position);
                    //Toast.makeText(context, "dddd", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, ListOfNotes.class);
                    /*intent.putExtra("NUMBER",callLog.getNumber());
                    intent.putExtra("NAME",callLog.getName());
                    intent.putExtra("DATE",String.valueOf(callLog.getDate()));
                    intent.putExtra("POSITION",position);*/
                    context.startActivity(intent);

                }
            });
            itemBinding.imagedeletenote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DatabaseHelper databaseHelper= new DatabaseHelper(ClassStatic.activity);
                    int id = databaseHelper.deleteEntry(String.valueOf(callLog.getDate()));
                    int id1 = databaseHelper.deleteVoiceEntry(String.valueOf(callLog.getDate()));
                    int id2 = databaseHelper.deletestatus(String.valueOf(callLog.getDate()));

                    if (id>0 || id1>0||id2>0){
                        itemBinding.imageeditnote.setVisibility(View.GONE);
                        itemBinding.imagedeletenote.setVisibility(View.GONE);
                        itemBinding.textViewStatus.setText("");
                        itemBinding.imageaddnote.setVisibility(View.VISIBLE);
                        callLogInfoArrayList.get(position).setFlag(false);
                        callLogInfoArrayList.get(position).setStatus("");
                        notifyDataSetChanged();
                        Snackbar.make(recyclerView, "Deleted.", Snackbar.LENGTH_LONG).show();
                    }else {
                        Snackbar.make(recyclerView, "Error.", Snackbar.LENGTH_LONG).show();
                    }
                    //Toast.makeText(context, "dddd", Toast.LENGTH_SHORT).show();
                }
            });
            itemBinding.imageaddnote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, EditNoteActivity.class);
                    intent.putExtra("NUMBER",callLog.getNumber());
                    intent.putExtra("NAME",callLog.getName());
                    intent.putExtra("DATE",String.valueOf(callLog.getDate()));
                    intent.putExtra("POSITION",position);
                    context.startActivity(intent);
                 //   showDialogsds(String.valueOf(callLog.getDate()),callLog.getNumber(),callLog.getName(),itemBinding,position);
                   // showDialog(String.valueOf(callLog.getDate()),callLog.getNumber(),callLog.getName(),itemBinding,position);
                    //Toast.makeText(context, "dddd", Toast.LENGTH_SHORT).show();
                }
            });
            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null)
                        onItemClickListener.onItemClicked(callLog);
                }
            });
        }
    }

    public void showDialog(final String date, final String number, final String name, final ListRowBinding itembinding, final int position){
        final SugarModel model = new SugarModel();
        final DatabaseHelper databaseHelper= new DatabaseHelper(ClassStatic.activity);
//        SugarModel fetch = null;
        SugarModel fetch = databaseHelper.GETNOTE( date);
        final Dialog dialog = new Dialog(ClassStatic.activity);
        dialog.setContentView(R.layout.addnote_dialog);
        dialog.setTitle("Note");

        TextView save,cancle,delete,txtname,txtnumber;
        save =   dialog.findViewById(R.id.save);
        cancle   =   dialog.findViewById(R.id.cancel);
        delete   =   dialog.findViewById(R.id.delete);
        txtname   =   dialog.findViewById(R.id.name);
        txtnumber   =   dialog.findViewById(R.id.number);
        final EditText edit=(EditText)dialog.findViewById(R.id.grouptitle);
        txtname.setText(name);
        txtnumber.setText(number);
        if (fetch!=null)
        if (fetch.getNote()!=null)
            edit.setText(fetch.getNote());
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text=edit.getText().toString();
                model.setDate(date);
                model.setNumber(number);
                model.setExtra(name);
                model.setNote(text);

                if (edit.getText().toString().isEmpty()){
                    Snackbar.make(recyclerView, "Please write some Note.", Snackbar.LENGTH_LONG).show();
                    return;
                }
                boolean reault = databaseHelper.SAVENOTE(model);
                if (reault){
                     itembinding.imageeditnote.setVisibility(View.VISIBLE);
                     itembinding.imagedeletenote.setVisibility(View.VISIBLE);
                     itembinding.imageaddnote.setVisibility(View.GONE);
                     callLogInfoArrayList.get(position).setFlag(true);
                     notifyDataSetChanged();
                }
                    Snackbar.make(recyclerView, "Note saved.", Snackbar.LENGTH_LONG).show();
                //model.save();
                //List<SugarModel> s = SugarModel.findWithQuery(SugarModel.class,"SELECT * FROM SugarModel WHERE `date`="+mydate);
                ///t.makeText(getActivity().getApplicationContext(), ""+s, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                //name=text;

            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
/*        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = databaseHelper.deleteEntry(date);
                int id1 = databaseHelper.deleteVoiceEntry(date);
                dialog.dismiss();
                if (id>0 && id1>0){
                    itembinding.imageeditnote.setVisibility(View.GONE);
                    itembinding.imagedeletenote.setVisibility(View.GONE);
                    itembinding.imageaddnote.setVisibility(View.VISIBLE);
                    callLogInfoArrayList.get(position).setFlag(false);
                    notifyDataSetChanged();
                    edit.setText("");
                }
                else{
                    Snackbar.make(recyclerView, "Error in Deleting Note", Snackbar.LENGTH_LONG).show();
                }

            }
        });*/
        dialog.show();
    }


    public void showDialogsds(final String date, final String number, final String name, final ListRowBinding itembinding, final int position){


        final Dialog selectDialog=new Dialog(context);
        selectDialog.setContentView(R.layout.show_notes_voice_dailog);
        selectDialog.setTitle("Note");
        selectDialog.setCancelable(false);
        selectDialog.show();
        TextView cancel,name1,number1;
        final ImageView text,mic;
        Spinner spin;
        cancel   =   selectDialog.findViewById(R.id.cancel);
        name1   =   selectDialog.findViewById(R.id.name);
        number1   =   selectDialog.findViewById(R.id.number);
        text   =   selectDialog.findViewById(R.id.text);
        mic   =   selectDialog.findViewById(R.id.mic);
        DatabaseHelper helper1=new DatabaseHelper(context);
        final String filename= helper1.GETVoiceNOTE(date);
        if(filename.length()>4) {
            callLogInfoArrayList.get(position).setFlag(true);
            itembinding.imageeditnote.setVisibility(View.VISIBLE);
            itembinding.imagedeletenote.setVisibility(View.VISIBLE);
            itembinding.imageaddnote.setVisibility(View.GONE);
            mic.setImageResource(R.drawable.ic_play);
        }
        else {
            mic.setImageResource(R.drawable.ic_mic);
            callLogInfoArrayList.get(position).setFlag(false);
        }
        spin  =   selectDialog.findViewById(R.id.spin);
        number1.setText(number);
        name1.setText(name);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item,context.getResources().getStringArray(R.array.select_status));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();

            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    showDialog(date,number,name,itembinding,position);
            }
        });
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!filename.isEmpty()) {

                    Toast.makeText(context, "" + date, Toast.LENGTH_SHORT).show();
                    MediaPlayer mp = new MediaPlayer();
                    try {
                        mp.setDataSource(date);
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.start();
                }
                else {
                //    mic.setImageResource(R.drawable.ic_mic);
                    Toast.makeText(context, "No Voice record Found", Toast.LENGTH_SHORT).show();
//                    afterCallActivity.recordVoiceNote();
                    if (context instanceof AfterCallActivity) {
                        ((AfterCallActivity) context).recordVoiceNote();
                    }
/*
                    filenameSave= String.valueOf(date);
                    filePath=filePath+"/"+ filenameSave +".wav";
                    AndroidAudioRecorder.with(activity)
                            // Required

                            .setFilePath(filePath)
                            .setColor(color)
                            .setRequestCode(requestCode)

                            // Optional
                            .setSource(AudioSource.MIC)
                            .setChannel(AudioChannel.STEREO)
                            .setSampleRate(AudioSampleRate.HZ_48000)
                            .setAutoStart(true)
                            .setKeepDisplayOn(true)

                            // Start recording
                            .record();
*/

                }

            }
        });
    }


}