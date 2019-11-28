package com.b96software.schoolplannerapp.assignments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.model.Assignment;
import com.b96software.schoolplannerapp.util.BundleUtils;

import butterknife.ButterKnife;

public class EditAssignmentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        ButterKnife.bind(this);


        if(savedInstanceState == null)
        {
            Assignment assignment = getIntent().getParcelableExtra(BundleUtils.BUNDLE_ASSIGNMENT);

            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleUtils.BUNDLE_ASSIGNMENT, assignment);

            Fragment fragment = new EditAssignmentFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }
    }
}
