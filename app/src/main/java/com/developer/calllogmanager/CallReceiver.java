package com.developer.calllogmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import java.util.Date;

public class CallReceiver extends PhonecallReceiver {

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {

    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        Intent intent = new Intent(ctx,AfterCallActivity.class);
        intent.putExtra("call","");
        intent.putExtra("start",start.toString());
        intent.putExtra("end",end.toString());
        intent.putExtra("number",number);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    @Override
    protected void onOutgoingCallEnded(final Context ctx, final String number, final Date start, final Date end) {

        Intent intent = new Intent(ctx,AfterCallActivity.class);
        intent.putExtra("call","");
        intent.putExtra("start",start.toString());
        intent.putExtra("end",end.toString());
        intent.putExtra("number",number);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        Toast.makeText(ctx, "missed", Toast.LENGTH_SHORT).show();
    }

}