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
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Note;
import com.b96software.schoolplannerapp.pageradapters.DailyEventPager;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DailyFragment extends Fragment implements ItemClickedListener<Note> {

    //Constants
    private static final int LIST_DAY_COUNT = 100, CAL_DAY_START = -50;


    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private long startTime;
    private Unbinder unbinder;
    private Calendar cal;
    private ArrayList<Long> dates;
    private DailyEventPager adapter;
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
        Log.e("StartTime", new SimpleDateFormat(Utils.DISPLAY_DATE).format(startTime));


        //Initialize and retrieve dates
        dates = new ArrayList<>();
        dates.addAll(getDates());

        //Create the pager adapter
        adapter = new DailyEventPager(getChildFragmentManager(), dates);
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
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        viewPager.setCurrentItem(LIST_DAY_COUNT / 2);


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

    @Override
    public void onItemClicked(Note item) {

    }

    private ArrayList<Long> getDates()
    {
        startTime = cal.getTimeInMillis();
        ArrayList<Long> dates = new ArrayList<>();

        //Decrement the date by 50
        cal.add(Calendar.DATE, CAL_DAY_START);


        //Log.e("CalStartDate", new SimpleDateFormat(Utils.DISPLAY_DATE).format(cal.getTimeInMillis()));

        for(int i = 0; i < LIST_DAY_COUNT; i++)
        {
            dates.add(cal.getTimeInMillis());
            cal.add(Calendar.DATE, 1);
        }

        cal.setTimeInMillis(startTime);
        return dates;
    }


}
