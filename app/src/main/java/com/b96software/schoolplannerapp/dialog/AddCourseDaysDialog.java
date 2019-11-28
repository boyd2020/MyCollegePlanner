package com.b96software.schoolplannerapp.dialog;

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
import com.b96software.schoolplannerapp.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddCourseDaysDialog extends DialogFragment implements View.OnClickListener
{

    @BindView(R.id.dialogTitle)
    TextView title;

    @BindView(R.id.dialogAdd)
    TextView add;

    @BindView(R.id.dialogRecyclerView)
    RecyclerView recyclerView;


    //Objects
    private Unbinder unbinder;
    private DayAdapter adapter;
    private ClassDaysSelectedListener callback;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (ClassDaysSelectedListener) getTargetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_select, container, false);
        unbinder = ButterKnife.bind(this, v);

        //Set Dialog Title
        title.setText(getString(R.string.course_select));
        add.setText(getString(R.string.select));

        //Initialize Adapter
        adapter = new DayAdapter(getContext(), getClassDays());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private ArrayList<Day> getClassDays()
    {
        String[] dayNames = new String[]{getString(R.string.mon), getString(R.string.tues),
            getString(R.string.wens), getString(R.string.thurs), getString(R.string.fri),
            getString(R.string.sat), getString(R.string.sun)};

        int[] dayValues = new int[]{Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY,
                Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY};

        ArrayList<Day> days = new ArrayList<>();

        for(int i = 0; i < dayNames.length; i++)
        {
            Day day = new Day(dayNames[i], dayValues[i], Utils.NOT_CHECKED);
            days.add(day);
        }

        return days;
    }





}
