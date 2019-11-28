package com.b96software.schoolplannerapp.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.assignments.AssignmentTabActivity;
import com.b96software.schoolplannerapp.course.CourseTabActivity;
import com.b96software.schoolplannerapp.dialog.BottomMenuDialogFragment;
import com.b96software.schoolplannerapp.grades.GradeTabActivity;
import com.b96software.schoolplannerapp.interfaces.DateChangedListener;
import com.b96software.schoolplannerapp.model.SpinnerItem;
import com.b96software.schoolplannerapp.notes.NoteTabActivity;
import com.b96software.schoolplannerapp.professors.ProfessorTabActivity;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.ServiceUtils;
import com.b96software.schoolplannerapp.util.Utils;
import com.b96software.schoolplannerapp.adapter.SpinnerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends Fragment implements View.OnClickListener, DateChangedListener, NavigationView.OnNavigationItemSelectedListener {

    //Constants
    private static final String TAG = "agenda";
    private static final int AGENDA_DAY = 0, AGENDA_WEEK = 1, AGENDA_MONTH = 2;

    //@BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.agendaSpinner) Spinner spinner;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navigationView) NavigationView navigationView;
    @BindView(R.id.adView) AdView mAdView;

    //Objects/Variables
    private long startTime;
    private int spinnerPosition;
    private Unbinder unbinder;
    private Calendar cal;
    private SpinnerAdapter adapter;
    private InterstitialAd mInterstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_agenda, container, false);
        unbinder = ButterKnife.bind(this, v);

        //Add Toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.ad_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.e("Ad Error", String.valueOf(i));
            }


            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.e("Ad Loaded", "Correctly");
            }

            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }



        });

        cal = new GregorianCalendar();

        if(savedInstanceState == null)
            startTime = new GregorianCalendar().getTimeInMillis();
        else
            startTime = savedInstanceState.getLong(BundleUtils.BUNDLE_TIME);


        cal.setTimeInMillis(startTime);
        //Log.e("StartTime", new SimpleDateFormat(Utils.DISPLAY_DATE).format(startTime));

        //Initialize Spinner Adapter
        adapter = new SpinnerAdapter(getContext(), R.layout.card_spinner, new ArrayList<SpinnerItem>());

        navigationView.getMenu().getItem(0).setChecked(true);

        addSpinner();
        addNavigationDrawer();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(BundleUtils.BUNDLE_TIME, startTime);
    }

    @Override
    public void onDateChanged(long date) {
        startTime = date;
        cal.setTimeInMillis(startTime);
        adapter.setItems(getSpinnerData());
    }

    @OnClick(R.id.fab)
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fab:
                BottomSheetDialogFragment fragment = new BottomMenuDialogFragment();
                fragment.show(getChildFragmentManager(), "MENU");
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId())
        {
            case R.id.menu_agenda:
                break;

            case R.id.menu_professor:
                startActivity(new Intent(getContext(), ProfessorTabActivity.class));
                break;

            case R.id.menu_courses:
                startActivity(new Intent(getContext(), CourseTabActivity.class));
                break;

            case R.id.menu_assignment:
                intent = new Intent(getContext(), AssignmentTabActivity.class);
                intent.putExtra(BundleUtils.BUNDLE_ASSIGNMENT_TYPE, Utils.ASSIGN_TYPE_HOMEWORK);
                startActivity(intent);
                break;

            case R.id.menu_exam:
                intent = new Intent(getContext(), AssignmentTabActivity.class);
                intent.putExtra(BundleUtils.BUNDLE_ASSIGNMENT_TYPE, Utils.ASSIGN_TYPE_EXAM);
                startActivity(intent);
                break;

            case R.id.menu_grades:
                startActivity(new Intent(getContext(), GradeTabActivity.class));
                break;

            case R.id.menu_notes:
                startActivity(new Intent(getContext(), NoteTabActivity.class));
                break;

            case R.id.menu_help:
                ServiceUtils.sendEmail(getContext());
                break;

            case R.id.menu_rate:
                ServiceUtils.rateApp(getContext());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addNavigationDrawer()
    {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }


    private ArrayList<SpinnerItem> getSpinnerData()
    {
        startTime = cal.getTimeInMillis();

        SimpleDateFormat format = new SimpleDateFormat(Utils.DATE_MONTH_DAY);

        //Get Day String
        String day = format.format(cal.getTimeInMillis());

        //Get Month String
        String month = new SimpleDateFormat(Utils.DATE_MONTH).format(cal.getTimeInMillis());

        //Get Week String
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            cal.add(Calendar.DATE, -6);


        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //Get First day of the week
        String week = format.format(cal.getTimeInMillis());

        cal.add(Calendar.DATE, 6);     //Get Last day of the week (Sunday)

        week += " - " + new SimpleDateFormat(Utils.DATE_DAY_NUM).format(cal.getTimeInMillis());

        //Reset Calendar Time
        cal.setTimeInMillis(startTime);

        ArrayList<SpinnerItem> items = new ArrayList<>();

        items.add(AGENDA_DAY, new SpinnerItem(getString(R.string.spinner_day), day));
        items.add(AGENDA_WEEK, new SpinnerItem(getString(R.string.spinner_week), week));
        items.add(AGENDA_MONTH, new SpinnerItem(getString(R.string.spinner_month), month));

        return items;
    }


    private void addSpinner()
    {
        //Get Spinner data and add the SpinnerAdapter
        adapter.addItems(getSpinnerData());
        spinner.setAdapter(adapter);

        addAgendaFragment();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPosition = position;
                addAgendaFragment();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void addAgendaFragment()
    {
        if(getChildFragmentManager().findFragmentByTag(TAG) != null)
            getChildFragmentManager().popBackStackImmediate();

        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleUtils.BUNDLE_TIME, startTime);

        switch (spinnerPosition)
        {
            case AGENDA_DAY:
                fragment = new DailyFragment();
                break;

            case AGENDA_WEEK:
                fragment = new WeeklyFragment();
                break;

            case AGENDA_MONTH:
                fragment = new MonthlyFragment();
                break;

            default:
                fragment = new WeeklyFragment();
                break;
        }

        fragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment, TAG).commit();
    }

}
