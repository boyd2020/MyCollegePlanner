package com.b96software.schoolplannerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.b96software.schoolplannerapp.welcome.WelcomeFragment;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        if(savedInstanceState == null)
        {
            WelcomeFragment fragment = new WelcomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();
        }
    }
}
