package com.developer.calllogmanager.voiceupdate.prefrence;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SessionManager {
	private static String TAG = SessionManager.class.getSimpleName();
	SharedPreferences
			pref;
	Editor
			editor;
	Context
			_context;
	private static final String PREF_NAME = "MyNotesPref";

	private static final String FLAG = "flag";


	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		editor = pref.edit();
	}

	public void setFlag(boolean code) {
		editor.putBoolean(FLAG, code);
		editor.apply();
	}
    public boolean getFlag(){
        return pref.getBoolean(FLAG,false);
    }
}
