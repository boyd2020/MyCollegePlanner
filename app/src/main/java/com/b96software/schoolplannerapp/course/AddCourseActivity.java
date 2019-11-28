package com.b96software.schoolplannerapp.course;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.b96software.schoolplannerapp.R;

import butterknife.ButterKnife;

public class AddCourseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        ButterKnife.bind(this);

        if(savedInstanceState == null)
        {
            Fragment fragment = new AddCourseFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }
    }
}
