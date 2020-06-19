package com.developer.calllogmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AllCallsReceiver extends BroadcastReceiver {
    Context pcontext;
    static MyPhoneStateListener PhoneListener;
    private SharedPreferences pref;
    public void onReceive(Context context, Intent intent) {
        try {
            pcontext = context;
            // TELEPHONY MANAGER class object to register one listner
            TelephonyManager tmgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            //Create Listner
            if (PhoneListener == null) {
                PhoneListener = new MyPhoneStateListener();
                // Register listener for LISTEN_CALL_STATE
                tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            }
        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }
    }
    private class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_IDLE) {

                    //showDialog("","","");
                    Intent intent = new Intent(pcontext,AfterCallActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    pcontext.startActivity(intent);
                }
            }
    }
    public void destroyListener(){

    }

}