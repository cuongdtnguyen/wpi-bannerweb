package com.cuongnd.wpibannerweb.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cuongnd.wpibannerweb.LoginActivity;

/**
 * Created by Cuong Nguyen on 5/10/2015.
 */
public class SessionManager {
    private static final String TAG = "SessionManager";
    private static SessionManager sessionManager;

    public static SessionManager getInstance(Context context) {
        if (sessionManager == null)
            sessionManager = new SessionManager(context);
        return sessionManager;
    }

    private static final String PREF_SID = "username";
    private static final String PREF_PIN = "password";

    private Context mContext;
    private SharedPreferences mPref;
    private ConnectionManager mConnectionManager;

    private SessionManager(Context context) {
        mContext = context;
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        mConnectionManager = ConnectionManager.getInstance();
    }

    public void createSession(String username, String pin) {
        mPref.edit()
            .putString(PREF_SID, username)
            .putString(PREF_PIN, pin)
            .apply();
    }

    public void checkStatus() {
        String username = mPref.getString(PREF_SID, null);
        String password = mPref.getString(PREF_PIN, null);
        if (username == null || password == null) {
            startLoginActivity();
        } else {
            mConnectionManager.setUsernameAndPin(username, password);
        }
    }

    public void deactivate() {
        new Thread() {
            @Override
            public void run() {
                mPref.edit().clear().apply();
                mConnectionManager.logOut();
            }
        }.start();
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent i = new Intent(mContext, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }

    public String getUserName() {
        return mPref.getString(PREF_SID, null);
    }
}