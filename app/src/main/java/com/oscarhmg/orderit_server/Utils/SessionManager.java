package com.oscarhmg.orderit_server.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.oscarhmg.orderit_server.activities.Home;
import com.oscarhmg.orderit_server.activities.MainActivity;
import com.oscarhmg.orderit_server.activities.SignIn;

import java.util.HashMap;

/**
 * Created by OscarHMG on 26/11/2017.
 */

public class SessionManager {
    private static final String TAG = SessionManager.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private Editor editor;
    private Context context;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "userLogged";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_NUMBER = "number";


    /**
     * Constructor of Session
     * @param context
     */
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String name, String phoneNumber) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_NAME, name);
        // Storing email in pref
        editor.putString(KEY_NUMBER, phoneNumber);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status If false it will redirect
     * user to login page Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!isLoggedIn()) {// user is not logged in redirect him to Login Activity

            Log.i(TAG, "Redirect to Login Activity");
            Intent intentLogin = new Intent(context, MainActivity.class);
            // Closing all the Activities
            intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(intentLogin);
        }else{//Redirect to principal menu

            Log.i(TAG, "Welcome, " +this.getUserDetails().get(KEY_NAME));
            Intent intentHomeMenu = new Intent(context, Home.class);
            // Closing all the Activities
            intentHomeMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intentHomeMenu.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Home Menu Activity
            context.startActivity(intentHomeMenu);
        }

    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, sharedPreferences.getString(KEY_NAME, null));

        // user phone id
        user.put(KEY_NUMBER, sharedPreferences.getString(KEY_NUMBER, null));

        // return user
        return user;
    }

    /**
     * Clear session details, and redirect to Sign In activity
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, SignIn.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }


}
