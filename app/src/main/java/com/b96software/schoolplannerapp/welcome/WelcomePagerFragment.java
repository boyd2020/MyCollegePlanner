package com.b96software.schoolplannerapp.welcome;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.util.BundleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WelcomePagerFragment extends Fragment {

    @BindView(R.id.welcomeTitle)
    TextView welcomeTitle;

    @BindView(R.id.welcomeDesc)
    TextView welcomeDesc;

    @BindView(R.id.welcomeImage)
    ImageView welcomeImage;

    @BindView(R.id.welcomePagerFragment)
    LinearLayout welcomePagerFragment;

    //Objects/Variables
    private int image, background;
    private String title, desc;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_fragment_pager, container, false);
        unbinder = ButterKnife.bind(this, v);


        if(savedInstanceState == null) {
            title = getArguments().getString(BundleUtils.BUNDLE_WELCOME_TITLE);
            desc = getArguments().getString(BundleUtils.BUNDLE_WELCOME_DESC);
            image = getArguments().getInt(BundleUtils.BUNDLE_WELCOME_IMAGE);
            background = getArguments().getInt(BundleUtils.BUNDLE_WELCOME_BACKGROUND);
        }
        else
        {
            title = savedInstanceState.getString(BundleUtils.BUNDLE_WELCOME_TITLE);
            desc = savedInstanceState.getString(BundleUtils.BUNDLE_WELCOME_DESC);
            image = savedInstanceState.getInt(BundleUtils.BUNDLE_WELCOME_IMAGE);
            background = savedInstanceState.getInt(BundleUtils.BUNDLE_WELCOME_BACKGROUND);
        }

        welcomeTitle.setText(title);
        welcomeDesc.setText(desc);
        welcomeImage.setImageResource(image);
        welcomePagerFragment.setBackgroundColor(getResources().getColor(background));

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BundleUtils.BUNDLE_WELCOME_TITLE, title);
        outState.putString(BundleUtils.BUNDLE_WELCOME_DESC, desc);
        outState.putInt(BundleUtils.BUNDLE_WELCOME_IMAGE, image);
        outState.putInt(BundleUtils.BUNDLE_WELCOME_BACKGROUND, background);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
