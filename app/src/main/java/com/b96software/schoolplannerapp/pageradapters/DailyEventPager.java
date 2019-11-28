package com.b96software.schoolplannerapp.pageradapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.b96software.schoolplannerapp.pagerfragments.DayPagerFragment;
import com.b96software.schoolplannerapp.util.Utils;

import java.util.ArrayList;

public class DailyEventPager extends FragmentStatePagerAdapter {

    //Constants
    public static final int DAYS = 2;

    //Weekdays
    private ArrayList<Long> dates;

    public DailyEventPager(FragmentManager fm, ArrayList<Long> dates) {
        super(fm);
        this.dates = dates;
    }

    @Override
    public Fragment getItem(int position) {

        //Gets the loaderID for the cursor
        int loaderID = (position + 1) * DAYS;

        Bundle bundle = new Bundle();
        bundle.putInt(Utils.KEY_LOADER_ID, loaderID);
        bundle.putLong(Utils.KEY_DATE, dates.get(position));
        Fragment fragment = new DayPagerFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return dates.size();
    }
}
