package com.example.victim.chatsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences pref = PreferenceManager
                        .getDefaultSharedPreferences(splash.this);
                int a = pref.getInt("LOGIN",0);
                if(a==1){startActivity(new Intent(splash.this, Login.class));}
                else{
                    UserDetails.username=pref.getString("username","anand");

                    startActivity(new Intent(splash.this,model1.class));}

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
