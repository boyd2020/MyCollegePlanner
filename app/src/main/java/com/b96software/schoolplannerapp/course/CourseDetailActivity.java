package com.b96software.schoolplannerapp.course;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.model.Course;
import com.b96software.schoolplannerapp.util.BundleUtils;

import butterknife.ButterKnife;

public class CourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        ButterKnife.bind(this);


        if(savedInstanceState == null)
        {
            Course course = getIntent().getParcelableExtra(BundleUtils.BUNDLE_COURSE);

            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleUtils.BUNDLE_COURSE, course);

            Fragment fragment = new CourseDetailFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }
    }
}
