package com.b96software.schoolplannerapp.notes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.model.Note;
import com.b96software.schoolplannerapp.util.BundleUtils;
import butterknife.ButterKnife;

public class EditNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        ButterKnife.bind(this);


        if(savedInstanceState == null)
        {
            Note note = getIntent().getParcelableExtra(BundleUtils.BUNDLE_NOTE);

            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleUtils.BUNDLE_NOTE, note);

            Fragment fragment = new EditNoteFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }
    }
}
