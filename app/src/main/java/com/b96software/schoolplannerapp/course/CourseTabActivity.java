package com.b96software.schoolplannerapp.course;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.b96software.schoolplannerapp.MainActivity;
import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.adapter.CourseAdapter;
import com.b96software.schoolplannerapp.assignments.AssignmentTabActivity;
import com.b96software.schoolplannerapp.grades.GradeTabActivity;
import com.b96software.schoolplannerapp.handlers.CourseHandler;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Course;
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

public class CourseTabActivity extends AppCompatActivity implements ItemClickedListener<Object>, LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{

    //Constants
    private static final int LOADER_ID = 1000000;
    private static final String TAG = "COURSE";

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
    private CourseAdapter adapter;
    private CourseHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(getString(R.string.course_tab_title));

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Initialize Handler
        handler = new CourseHandler(this);

        //Initialize RecyclerView Adapter
        adapter = new CourseAdapter(this, R.layout.card_course, new ArrayList<Course>(), this);
        emptyText.setText(getString(R.string.course_empty));



        //Add components to RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        fab.setImageResource(R.drawable.baseline_class_black_48dp);

        getSupportLoaderManager().destroyLoader(LOADER_ID);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);


        navigationView.getMenu().getItem(2).setChecked(true);
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
                addCourse();
                break;
        }
    }

    @Override
    public void onItemClicked(Object object) {
        Course course = (Course)object;

        if(contentFrame != null)
            openFragment(course);
        else
            openActivity(course);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int position, Bundle bundle) {
        return handler.getAllCourses();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ArrayList<Course> courses = handler.getCoursesFromCursor(cursor);
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



    private void openFragment(Course course)
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
        bundle.putParcelable(BundleUtils.BUNDLE_COURSE, course);

        Fragment fragment = new CourseDetailFragment();
        fragment.setArguments(bundle);


        //Enter Transition
        Fade fade = new Fade();
        fade.setDuration(400);
        fragment.setEnterTransition(fade);

        getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, fragment, TAG).commit();
    }

    private void openActivity(Course note)
    {
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra(BundleUtils.BUNDLE_COURSE, note);
        startActivity(intent);
    }

    private void addCourse()
    {
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }


}
