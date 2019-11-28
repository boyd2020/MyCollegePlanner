package com.b96software.schoolplannerapp.grades;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.transition.Fade;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.b96software.schoolplannerapp.MainActivity;
import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.adapter.GradeAdapter;
import com.b96software.schoolplannerapp.assignments.AssignmentTabActivity;
import com.b96software.schoolplannerapp.course.CourseTabActivity;
import com.b96software.schoolplannerapp.handlers.GradeHandler;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Grade;
import com.b96software.schoolplannerapp.notes.NoteTabActivity;
import com.b96software.schoolplannerapp.professors.ProfessorTabActivity;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.ServiceUtils;
import com.b96software.schoolplannerapp.util.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GradeTabActivity extends AppCompatActivity implements ItemClickedListener<Object>, LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{

    //Constants
    private static final int LOADER_ID = 1000054;
    private static final String TAG = "Grade";

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

    @BindView(R.id.navigationView) NavigationView navigationView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.adView)
    AdView mAdView;

    //Objects/Variables
    private GradeAdapter adapter;
    private GradeHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(getString(R.string.grade_tab_title));

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Initialize Handler
        handler = new GradeHandler(this);

        //Initialize RecyclerView Adapter
        adapter = new GradeAdapter(this, R.layout.card_grade, new ArrayList<Grade>(), this);
        emptyText.setText(getString(R.string.grade_empty));


        //Add components to RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getSupportLoaderManager().destroyLoader(LOADER_ID);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);


        fab.setImageResource(R.drawable.baseline_star_rate_black_48dp);
        navigationView.getMenu().getItem(5).setChecked(true);
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
                addGrade();
                break;
        }
    }

    @Override
    public void onItemClicked(Object object) {
        Grade grade = (Grade)object;

        if(contentFrame != null)
            openFragment(grade);
        else
            openActivity(grade);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int position, Bundle bundle) {
        return handler.getAllGradesFromDB();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.e("Course Size", String.valueOf(cursor.getCount()));
        ArrayList<Grade> courses = handler.getGradesFromCursor(cursor);
        adapter.setCursor(courses);

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



    private void openFragment(Grade grade)
    {
        //Exit Animation
        Fragment previousFragment = getSupportFragmentManager().findFragmentByTag(TAG);

        if(previousFragment != null) {
            //Exit Transition
            Fade exitFade = new Fade();
            exitFade.setDuration(400);
            previousFragment.setExitTransition(exitFade);
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleUtils.BUNDLE_GRADE, grade);

        Fragment fragment = new GradeOverviewFragment();
        fragment.setArguments(bundle);


        //Enter Transition
        Fade fade = new Fade();
        fade.setDuration(400);
        fragment.setEnterTransition(fade);

        getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, fragment, TAG).commit();
    }

    private void openActivity(Grade grade)
    {
        Intent intent = new Intent(this, GradeOverviewActivity.class);
        intent.putExtra(BundleUtils.BUNDLE_GRADE, grade);
        startActivity(intent);
    }

    private void addGrade()
    {
        Intent intent = new Intent(this, AddGradeActivity.class);
        startActivity(intent);
    }


}
