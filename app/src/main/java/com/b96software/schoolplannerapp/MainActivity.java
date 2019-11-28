package com.b96software.schoolplannerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.b96software.schoolplannerapp.agenda.HomeFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        MobileAds.initialize(this, getString(R.string.app_id));

        //deleteDatabase(Constants.DATABASE_NAME);

        if(savedInstanceState == null)
        {
            HomeFragment fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();
        }


    }
}
