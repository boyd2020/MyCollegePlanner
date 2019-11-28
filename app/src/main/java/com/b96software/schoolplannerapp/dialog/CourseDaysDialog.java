package com.b96software.schoolplannerapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.adapter.DayAdapter;
import com.b96software.schoolplannerapp.interfaces.ClassDaysSelectedListener;
import com.b96software.schoolplannerapp.model.Day;
import com.b96software.schoolplannerapp.util.BundleUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CourseDaysDialog extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.dialogRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.dialogAdd)
    TextView dialogAdd;

    @BindView(R.id.dialogTitle)
    TextView dialogTitle;

    //Objects/Variables
    private Unbinder unbinder;
    private DayAdapter adapter;
    private ArrayList<Day> days;
    private ClassDaysSelectedListener callback;


    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();

        int deviceWidth = (int) (getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density);

        if(d != null && deviceWidth < 600)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (ClassDaysSelectedListener) getTargetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_select, container, false);
        unbinder = ButterKnife.bind(this, v);

        if(savedInstanceState == null)
            days = getArguments().getParcelableArrayList(BundleUtils.BUNDLE_CLASS_DATES);
        else
            days = savedInstanceState.getParcelableArrayList(BundleUtils.BUNDLE_CLASS_DATES);

        adapter = new DayAdapter(getContext(), days);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        dialogAdd.setText(getString(R.string.add));
        dialogTitle.setText(getString(R.string.course_days));


        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BundleUtils.BUNDLE_CLASS_DATES, days);
    }

    @OnClick({R.id.dialogAdd, R.id.dialogCancel})
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.dialogAdd:
                callback.OnClassDaysSelected(adapter.getDays());
                dismiss();
                break;

            case R.id.dialogCancel:
                dismiss();
                break;
        }
    }
}
