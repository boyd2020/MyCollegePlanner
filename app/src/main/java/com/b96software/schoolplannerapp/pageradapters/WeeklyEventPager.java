package com.b96software.schoolplannerapp.pageradapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.b96software.schoolplannerapp.pagerfragments.WeeklyPagerFragment;
import com.b96software.schoolplannerapp.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class WeeklyEventPager extends FragmentStatePagerAdapter {

    //Constants
    public static final int CURSORS = 8, WEEK = 7;

    //Start each Week
    private Calendar cal;
    private ArrayList<Long> dates;

    public WeeklyEventPager(FragmentManager fm, ArrayList<Long> dates) {
        super(fm);
        this.dates = dates;
        this.cal = new GregorianCalendar();
    }

    @Override
    public Fragment getItem(int position) {

        ArrayList<Long> weeklyDates = new ArrayList<>();

        //Gets the loaderID for the cursor
        int loaderID = (position + 1) * CURSORS;

        //Set the calendar date
        cal.setTimeInMillis(dates.get(position));

        for(int i = 0; i < WEEK; i++)
        {
            weeklyDates.add(cal.getTimeInMillis());     //Add current date to the list
            cal.add(Calendar.DATE, 1);          //Increment the date
        }

        Bundle bundle = new Bundle();
        bundle.putInt(Utils.KEY_LOADER_ID, loaderID);
        bundle.putSerializable(Utils.KEY_DATES, weeklyDates);
        Fragment fragment = new WeeklyPagerFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public void updateDates(ArrayList<Long> d)
    {
        dates.clear();
        dates.addAll(d);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dates.size();
    }

}
