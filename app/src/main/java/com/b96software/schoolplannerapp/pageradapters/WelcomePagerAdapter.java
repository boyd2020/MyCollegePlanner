package com.b96software.schoolplannerapp.pageradapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.welcome.WelcomePagerFragment;

public class WelcomePagerAdapter extends FragmentPagerAdapter {

    private int[] images, backgrounds;
    private String[] titles, descs;


    public WelcomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        titles = new String[]{context.getString(R.string.slide_1_title), context.getString(R.string.slide_2_title),
                context.getString(R.string.slide_3_title), context.getString(R.string.slide_4_title), context.getString(R.string.slide_5_title)};

        descs = new String[]{context.getString(R.string.slide_1_desc), context.getString(R.string.slide_2_desc),
                context.getString(R.string.slide_3_desc), context.getString(R.string.slide_4_desc), context.getString(R.string.slide_5_desc)};


        images = new int[] {R.drawable.web_hi_res_512, R.drawable.ic_assignment_black_48dp,
                R.drawable.baseline_notifications_white_48dp, R.drawable.baseline_widgets_white_48dp, R.drawable.web_hi_res_512};

        backgrounds = new int[]{R.color.bg_screen1, R.color.bg_screen2, R.color.bg_screen3, R.color.bg_screen4,
                R.color.bg_screen5};
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(BundleUtils.BUNDLE_WELCOME_TITLE, titles[position]);
        bundle.putString(BundleUtils.BUNDLE_WELCOME_DESC, descs[position]);
        bundle.putInt(BundleUtils.BUNDLE_WELCOME_IMAGE, images[position]);
        bundle.putInt(BundleUtils.BUNDLE_WELCOME_BACKGROUND, backgrounds[position]);

        Fragment fragment = new WelcomePagerFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
