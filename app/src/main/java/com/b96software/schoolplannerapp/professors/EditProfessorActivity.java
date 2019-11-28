package com.b96software.schoolplannerapp.professors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.model.Professor;
import com.b96software.schoolplannerapp.util.BundleUtils;

import butterknife.ButterKnife;

public class EditProfessorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        ButterKnife.bind(this);


        if(savedInstanceState == null)
        {
            Professor professor = getIntent().getParcelableExtra(BundleUtils.BUNDLE_PROFESSOR);

            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleUtils.BUNDLE_PROFESSOR, professor);

            Fragment fragment = new EditProfessorFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }
    }
}
