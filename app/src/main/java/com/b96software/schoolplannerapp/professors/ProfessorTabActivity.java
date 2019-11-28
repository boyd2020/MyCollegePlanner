package com.b96software.schoolplannerapp.professors;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.transition.Explode;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.b96software.schoolplannerapp.MainActivity;
import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.adapter.ProfessorAdapter;
import com.b96software.schoolplannerapp.assignments.AssignmentTabActivity;
import com.b96software.schoolplannerapp.course.CourseTabActivity;
import com.b96software.schoolplannerapp.grades.GradeTabActivity;
import com.b96software.schoolplannerapp.handlers.ProfessorHandler;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Professor;
import com.b96software.schoolplannerapp.notes.NoteTabActivity;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.ServiceUtils;
import com.b96software.schoolplannerapp.util.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfessorTabActivity extends AppCompatActivity implements ItemClickedListener<Professor>,
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    //Constants
    private static final int LOADER_ID = 1000000;
    private static final String TAG = "professor";

    //Views
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyText)
    TextView emptyText;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.contentFrame)
    FrameLayout contentFrame;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigationView)
    NavigationView navigationView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.adView)
    AdView mAdView;

    //Objects/Variables
    private ProfessorAdapter adapter;
    private ProfessorHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(getString(R.string.prof_tab_title));

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Initialize Handler
        handler = new ProfessorHandler(this);

        //Initialize RecyclerView Adapter
        adapter = new ProfessorAdapter(this, R.layout.card_professor, new ArrayList<Professor>(), this);


        //Add components to RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        emptyText.setText(getString(R.string.prof_empty));
        fab.setImageResource(R.drawable.ic_person_white_48dp);


        //Reset the loader
        getSupportLoaderManager().destroyLoader(LOADER_ID);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        //Pop the fragment (if available)
        if(getSupportFragmentManager().findFragmentByTag(TAG) != null)
            getSupportFragmentManager().popBackStack();


        navigationView.getMenu().getItem(1).setChecked(true);

        //Add Navigation Drawer
        addNavigationDrawer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOADER_ID);
    }

    @OnClick(R.id.fab)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fab:
                addProfessor();
                break;
        }
    }

    @Override
    public void onItemClicked(Professor professor) {

        if(contentFrame != null)
            openFragment(professor);
        else
            openActivity(professor);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int position, Bundle bundle) {
        return handler.getAllProfessorsFromDB();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ArrayList<Professor> professors = handler.getProfessorsFromCursor(cursor);
        adapter.setCursor(professors);

        if(cursor.getCount() == 0)
            emptyText.setVisibility(View.VISIBLE);
        else
            emptyText.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId())
        {
            case R.id.menu_agenda:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

            case R.id.menu_professor:
                startActivity(new Intent(this, ProfessorTabActivity.class));
                finish();
                break;

            case R.id.menu_courses:
                startActivity(new Intent(this, CourseTabActivity.class));
                finish();
                break;

            case R.id.menu_assignment:
                intent = new Intent(this, AssignmentTabActivity.class);
                intent.putExtra(BundleUtils.BUNDLE_ASSIGNMENT_TYPE, Utils.ASSIGN_TYPE_HOMEWORK);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_exam:
                intent = new Intent(this, AssignmentTabActivity.class);
                intent.putExtra(BundleUtils.BUNDLE_ASSIGNMENT_TYPE, Utils.ASSIGN_TYPE_EXAM);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_grades:
                startActivity(new Intent(this, GradeTabActivity.class));
                finish();
                break;

            case R.id.menu_notes:
                startActivity(new Intent(this, NoteTabActivity.class));
                finish();
                break;

            case R.id.menu_help:
                ServiceUtils.sendEmail(this);
                break;

            case R.id.menu_rate:
                ServiceUtils.rateApp(this);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addNavigationDrawer()
    {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }



    private void openFragment(Professor professor)
    {
        //Exit Animation
        Fragment previousFragment = getSupportFragmentManager().findFragmentByTag(TAG);

        if(previousFragment != null) {
            //Exit Transition
            Explode exitFade = new Explode();
            exitFade.setDuration(500);
            previousFragment.setExitTransition(exitFade);
        }


        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleUtils.BUNDLE_PROFESSOR, professor);

        Explode explode = new Explode();
        explode.setStartDelay(500);
        explode.setDuration(500);


        Fragment fragment = new ProfessorOverviewFragment();
        fragment.setArguments(bundle);
        fragment.setEnterTransition(explode);

        getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, fragment, TAG).commit();
    }

    private void openActivity(Professor professor)
    {
        Intent intent = new Intent(this, ProfessorOverviewActivity.class);
        intent.putExtra(BundleUtils.BUNDLE_PROFESSOR, professor);
        startActivity(intent);
    }

    private void addProfessor()
    {
        Intent intent = new Intent(this, AddProfessorActivity.class);
        startActivity(intent);
    }


}
