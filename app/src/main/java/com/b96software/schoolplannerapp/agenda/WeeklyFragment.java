package com.b96software.schoolplannerapp.agenda;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.interfaces.DateChangedListener;
import com.b96software.schoolplannerapp.pageradapters.WeeklyEventPager;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WeeklyFragment extends Fragment {

    //Constants
    private static final int LIST_WEEK_COUNT = 100, CAL_WEEK_START = -350;


    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private long startTime;
    private Unbinder unbinder;
    private Calendar cal;
    private ArrayList<Long> dates;
    private WeeklyEventPager adapter;
    private DateChangedListener callback;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (DateChangedListener) getParentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pager_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        cal = new GregorianCalendar();

        if(savedInstanceState == null)
            startTime = getArguments().getLong(BundleUtils.BUNDLE_TIME);
        else
            startTime = savedInstanceState.getLong(BundleUtils.BUNDLE_TIME);


        cal.setTimeInMillis(startTime);
        //Log.e("StartTime", new SimpleDateFormat(Utils.DISPLAY_DATE).format(startTime));



        //Initialize and retrieve dates
        dates = new ArrayList<>();
        dates.addAll(getDates());

        //Create the pager adapter
        adapter = new WeeklyEventPager(getChildFragmentManager(), dates);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                startTime = dates.get(position);
                callback.onDateChanged(startTime);
                cal.setTimeInMillis(startTime);
                Log.e("Weekly Time Selected",new SimpleDateFormat(Utils.DISPLAY_DATE).format(startTime));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        viewPager.setCurrentItem(LIST_WEEK_COUNT / 2);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.e("OutStateTime", new SimpleDateFormat(Utils.DISPLAY_DATE).format(startTime));
        outState.putLong(BundleUtils.BUNDLE_TIME, startTime);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private ArrayList<Long> getDates()
    {
        startTime = cal.getTimeInMillis();
        ArrayList<Long> dates = new ArrayList<>();


        Log.e("CalStartDate", new SimpleDateFormat(Utils.DISPLAY_DATE).format(cal.getTimeInMillis()));


        //Get Week String
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            cal.add(Calendar.DATE, -6);

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //Get First day of the week

        //Decrement calendar by 350 days (50 Weeks)
        cal.add(Calendar.DATE, CAL_WEEK_START);


        for(int i = 0; i < LIST_WEEK_COUNT; i++)
        {
            dates.add(cal.getTimeInMillis());
            cal.add(Calendar.DATE, Utils.WEEK);
            //Log.e("Weekly Position: " + i, "Date " + format.format(calendar.getTimeInMillis()));
        }


        cal.setTimeInMillis(startTime);
        return dates;
    }
}
