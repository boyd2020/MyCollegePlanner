package com.b96software.schoolplannerapp.classdate;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.dialog.DatePickerDialog;
import com.b96software.schoolplannerapp.dialog.TimePickerDialog;
import com.b96software.schoolplannerapp.handlers.CourseHandler;
import com.b96software.schoolplannerapp.model.ClassDate;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditClassDateFragment extends Fragment implements View.OnClickListener,
        DatePickerDialog.OnDatePickListener, TimePickerDialog.OnTimeSelectedListener {


    @BindView(R.id.courseStartTime)
    TextView courseStartTime;

    @BindView(R.id.courseEndTime)
    TextView courseEndTime;

    @BindView(R.id.courseDate)
    TextView courseDate;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    //Variables/ Objects
    private int timeType;
    private Calendar cal;
    private Unbinder unbinder;
    private ClassDate classDate;
    private CourseHandler handler;
    private SimpleDateFormat timeFormat, dateFormat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_class_date_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        if(getActivity().findViewById(R.id.contentFrame) == null)
        {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

        if(savedInstanceState == null)
            classDate = getArguments().getParcelable(BundleUtils.BUNDLE_CLASS_DATE);
        else
            classDate = savedInstanceState.getParcelable(BundleUtils.BUNDLE_CLASS_DATE);

        cal = new GregorianCalendar();
        handler = new CourseHandler(getContext());


        timeFormat = new SimpleDateFormat(Utils.DATE_TIME);
        dateFormat = new SimpleDateFormat(Utils.SQL_DATETIME_FORMAT);


        //Toolbar Info
        collapsingToolbar.setTitle(classDate.getCourseName());
        collapsingToolbar.setBackgroundColor(classDate.getCourseColor());
        collapsingToolbar.setContentScrimColor(classDate.getCourseColor());
        toolbar.setBackgroundColor(classDate.getCourseColor());

        if(getActivity().findViewById(R.id.toolbar) != null)
            ((Toolbar)getActivity().findViewById(R.id.toolbar)).setBackgroundColor(classDate.getCourseColor());


        try {
            if(classDate.getClassDate() != null) {
                long date = dateFormat.parse(classDate.getClassDate()).getTime();
                cal.setTimeInMillis(date);
                courseDate.setText(new SimpleDateFormat(Utils.DISPLAY_DATE).format(date));
            }

            Log.e("Class Start", classDate.getClassStart());
            Log.e("Class End", classDate.getClassEnd());


            if(classDate.getClassStart() != null) {
                long startTime = dateFormat.parse(classDate.getClassStart()).getTime();
                courseStartTime.setText(timeFormat.format(startTime));

            }

            if(classDate.getClassEnd() != null) {
                long endTime = dateFormat.parse(classDate.getClassEnd()).getTime();
                courseEndTime.setText(timeFormat.format(endTime));
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BundleUtils.BUNDLE_CLASS_DATE, classDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fab, R.id.courseDate, R.id.courseStartRow, R.id.courseEndRow})
    @Override
    public void onClick(View v) {
        DialogFragment fragment;

        switch (v.getId())
        {
            case R.id.fab:
                updateClassDate();
                break;

            case R.id.courseDate:
                fragment = new DatePickerDialog();
                fragment.setTargetFragment(this, 0);
                fragment.show(getActivity().getSupportFragmentManager(), "START");
                break;

            case R.id.courseStartRow:
                timeType = Utils.COURSE_TIME_FROM;
                fragment = new TimePickerDialog();
                fragment.setTargetFragment(this, 0);
                fragment.show(getActivity().getSupportFragmentManager(), "START");
                break;

            case R.id.courseEndRow:
                timeType = Utils.COURSE_TIME_TO;
                fragment = new TimePickerDialog();
                fragment.setTargetFragment(this, 0);
                fragment.show(getActivity().getSupportFragmentManager(), "END");
                break;
        }
    }


    @Override
    public void onDatePick(int day, int month, int year) {
        cal.set(year, month, day);
        classDate.setClassDate(dateFormat.format(cal.getTimeInMillis()));
        courseDate.setText(new SimpleDateFormat(Utils.DISPLAY_DATE).format(cal.getTimeInMillis()));
    }

    @Override
    public void onTimeSelected(int hourOfDay, int minute) {
        cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.SQL_DATETIME_FORMAT);


        if(timeType == Utils.COURSE_TIME_FROM)
        {
            long startTime = cal.getTimeInMillis();
            courseStartTime.setText(timeFormat.format(startTime));
            classDate.setClassStart(dateFormat.format(startTime));
        }
        else
        {
            long endTime = cal.getTimeInMillis();
            courseEndTime.setText(timeFormat.format(endTime));
            classDate.setClassEnd(dateFormat.format(endTime));
        }
    }

    private void updateClassDate()
    {
        handler.updateClassDate(classDate);
        getActivity().finish();
    }
}
