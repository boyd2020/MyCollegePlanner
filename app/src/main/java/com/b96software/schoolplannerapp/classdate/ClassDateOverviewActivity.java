package com.b96software.schoolplannerapp.classdate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.model.ClassDate;
import com.b96software.schoolplannerapp.util.BundleUtils;

public class ClassDateOverviewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

        if(savedInstanceState == null)
        {
            ClassDate classDate = getIntent().getParcelableExtra(BundleUtils.BUNDLE_CLASS_DATE);

            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleUtils.BUNDLE_CLASS_DATE, classDate);

            Fragment fragment = new ClassDateOverviewFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }
    }
}
