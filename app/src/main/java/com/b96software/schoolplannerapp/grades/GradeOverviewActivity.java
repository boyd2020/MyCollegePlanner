package com.b96software.schoolplannerapp.grades;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.model.Grade;
import com.b96software.schoolplannerapp.util.BundleUtils;

public class GradeOverviewActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

        if(savedInstanceState == null)
        {
            Grade grade = getIntent().getParcelableExtra(BundleUtils.BUNDLE_GRADE);

            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleUtils.BUNDLE_GRADE, grade);

            Fragment fragment = new GradeOverviewFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }
    }
}
