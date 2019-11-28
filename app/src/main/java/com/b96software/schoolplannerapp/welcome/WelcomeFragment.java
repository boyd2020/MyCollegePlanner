package com.b96software.schoolplannerapp.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b96software.schoolplannerapp.MainActivity;
import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.managers.PreferenceManager;
import com.b96software.schoolplannerapp.pageradapters.WelcomePagerAdapter;
import com.b96software.schoolplannerapp.util.ServiceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WelcomeFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.nextButton)
    TextView nextButton;

    @BindView(R.id.skipButton)
    TextView skipButton;

    //Objects/Variables
    private Unbinder unbinder;
    private PreferenceManager manager;
    private WelcomePagerAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);


        manager = new PreferenceManager(getContext());
        adapter = new WelcomePagerAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(adapter);


        //Verify if user has used this application before
        if(!manager.isFirstTimeLaunch())
            launchHomeScreen();


        ServiceUtils.registerReceiver(getContext());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                if(position == adapter.getCount() - 1)
                {
                    // last page. make button text to GOT IT
                    nextButton.setText(getString(R.string.start));
                    skipButton.setVisibility(View.GONE);
                }
                else
                {
                    nextButton.setText(getString(R.string.next));
                    skipButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return v;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.skipButton, R.id.nextButton})
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.skipButton:
                launchHomeScreen();
                break;

            case R.id.nextButton:
                next();
                break;
        }
    }


    private void next()
    {
        // checking for last page. if last page home screen will be launched
        int current = viewPager.getCurrentItem() + 1;
        if (current < adapter.getCount()) {
            // move to next screen
            viewPager.setCurrentItem(current);
        } else {
            launchHomeScreen();
        }
    }

    private void launchHomeScreen()
    {
        manager.setFirstTimeLaunch(false);
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}
