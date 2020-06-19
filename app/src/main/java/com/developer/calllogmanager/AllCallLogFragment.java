package com.developer.calllogmanager;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.developer.calllogmanager.Models.SugarModel;
import com.developer.calllogmanager.databinding.CallLogFragmentBinding;
import com.developer.calllogmanager.dbHelper.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;

import static android.support.v4.content.FileProvider.getUriForFile;

public class AllCallLogFragment extends Fragment {

    CallLogFragmentBinding binding;
    CallLogAdapter adapter;
    SharedPreferences pref;
    CallLogAdapter.OnCallLogItemClickListener onItemClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.call_log_fragment, container, false);
        initComponents();
        ClassStatic.activity = getActivity();
        setHasOptionsMenu(true);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        return binding.getRoot();
    }

    public void initComponents() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
/*
        Drawable mDivider = ContextCompat.getDrawable(getActivity(), R.drawable.divider);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(mDivider);
        binding.recyclerView.addItemDecoration(mDividerItemDecoration);
*/

        adapter = new CallLogAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);
        loadData();
    }

    public void loadData() {
        final DatabaseHelper helper = new DatabaseHelper(getActivity());
        ArrayList<SugarModel> arrayList = new ArrayList<SugarModel>();
        ArrayList<CallLogInfo> callLogInfos = new ArrayList<>();
        adapter.addAllCallLog(callLogInfos, arrayList, binding.recyclerView);
        adapter.notifyDataSetChanged();
        arrayList = helper.FetchData();
        ArrayList<SugarModel> voiceArrayList = new ArrayList<>();
        voiceArrayList = helper.FetchVoiceData();
        callLogInfos = loadAllData();

        ArrayList<SugarModel> statusArrayList = new ArrayList<>();
        statusArrayList = helper.FetchAllStatus();
        if (statusArrayList != null) {
            for (int i = 0; i < callLogInfos.size(); i++) {
                //SugarModel s = helper.GETNOTE(String.valueOf(callLogInfos.get(i).getDate()));
                for (int j = 0; j < statusArrayList.size(); j++) {
                    if (statusArrayList.get(j).getDate().equals(String.valueOf(callLogInfos.get(i).getDate()))) {
                        callLogInfos.get(i).setStatus(statusArrayList.get(j).getNote());
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < callLogInfos.size(); i++) {
            //SugarModel s = helper.GETNOTE(String.valueOf(callLogInfos.get(i).getDate()));
            for (int j = 0; j < arrayList.size(); j++) {
                if (arrayList.get(j).getDate().equals(String.valueOf(callLogInfos.get(i).getDate()))) {
                    callLogInfos.get(i).setFlag(true);
                    break;
                }
            }
        }
        for (int i = 0; i < callLogInfos.size(); i++) {
            //SugarModel s = helper.GETNOTE(String.valueOf(callLogInfos.get(i).getDate()));
            for (int j = 0; j < voiceArrayList.size(); j++) {
                if (voiceArrayList.get(j).getDate().equals(String.valueOf(callLogInfos.get(i).getDate()))) {
                    callLogInfos.get(i).setFlag(true);
                    break;
                }
            }
        }
        adapter.addAllCallLog(callLogInfos, arrayList, binding.recyclerView);
        adapter.notifyDataSetChanged();
        onItemClickListener = new CallLogAdapter.OnCallLogItemClickListener() {
            @Override
            public void onItemClicked(CallLogInfo callLogInfo) {
                String number = callLogInfo.getNumber();
                String name = callLogInfo.getName();
                String date = String.valueOf(callLogInfo.getDate());
                //showDialog(date,number,name);

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

                String date12 = pref.getString("date", "");
                DatabaseHelper helper1 = new DatabaseHelper(getActivity().getApplicationContext());
                String filename = helper1.select_File(date);
                //Toast.makeText(getActivity(), ""+filename, Toast.LENGTH_SHORT).show();
                if (filename.length() > 4) {
                    String path = Environment.getExternalStorageDirectory() + "/My Records/" + new CallLogUtils(getActivity().getApplicationContext()).getDateaa() + "/" + filename;

                    Log.d("path", "onClick: " + path);
//                    Uri uri = Uri.parse(path);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File file = new File(filename);
//                    intent.setDataAndType(Uri.fromFile(file), "audio/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(getUriForFile(getActivity().getApplicationContext(), "com.developer.calllogmanager", file), "audio/*");
                    startActivity(intent);
                    pref.edit().putBoolean("pauseStateVLC", true).apply();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Recording not found", Toast.LENGTH_SHORT).show();
                }

            }
        };
        adapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                loadData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<CallLogInfo> loadAllData() {
        ArrayList<CallLogInfo> mainList = new ArrayList<>();

        String projection[] = {"_id", CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.TYPE, CallLog.Calls.CACHED_NAME};
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        @SuppressLint("MissingPermission") Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            Log.d("CALLLOG", "cursor is null");
            return null;
        }

        if (cursor.getCount() == 0) {
            Log.d("CALLLOG", "cursor size is 0");
            return null;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CallLogInfo callLogInfo = new CallLogInfo();
            callLogInfo.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
            callLogInfo.setNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
            callLogInfo.setCallType(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
            callLogInfo.setDate(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
            callLogInfo.setDuration(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)));
            mainList.add(callLogInfo);


            cursor.moveToNext();
        }
        cursor.close();
        return mainList;
    }
}
