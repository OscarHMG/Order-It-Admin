package com.oscarhmg.orderit_server.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.oscarhmg.orderit_server.Utils.SessionManager;

/**
 * Created by OscarHMG on 26/11/2017.
 */

public class SplashScreen extends AppCompatActivity {
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        session = new SessionManager(this);
        session.checkLogin();
        finish();
    }
}
