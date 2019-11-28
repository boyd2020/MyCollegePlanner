package com.b96software.schoolplannerapp.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.dialog.AddProfessorDialog;
import com.b96software.schoolplannerapp.dialog.CourseDaysDialog;
import com.b96software.schoolplannerapp.dialog.DatePickerDialog;
import com.b96software.schoolplannerapp.dialog.TimePickerDialog;
import com.b96software.schoolplannerapp.handlers.CourseHandler;
import com.b96software.schoolplannerapp.interfaces.ClassDaysSelectedListener;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Course;
import com.b96software.schoolplannerapp.model.Day;
import com.b96software.schoolplannerapp.model.Professor;
import com.b96software.schoolplannerapp.services.ClassDatesIntentService;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.ServiceUtils;
import com.b96software.schoolplannerapp.util.Utils;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditCourseFragment extends Fragment implements View.OnClickListener,
        TimePickerDialog.OnTimeSelectedListener, ItemClickedListener<Professor>, ClassDaysSelectedListener,
        DatePickerDialog.OnDatePickListener
{

    @BindView(R.id.courseName)
    EditText courseName;

    @BindView(R.id.courseColor)
    ImageView courseColor;

    @BindView(R.id.profName)
    TextView profName;

    @BindView(R.id.courseLocation)
    EditText courseLocation;

    @BindView(R.id.courseStartTime)
    TextView courseStart;

    @BindView(R.id.courseEndTime)
    TextView courseEnd;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.courseDays)
    TextView courseDays;

    @BindView(R.id.courseStartDate)
    TextView courseStartDate;

    @BindView(R.id.courseEndDate)
    TextView courseEndDate;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;


    //Objects
    private int timeType, dateType;
    private Calendar cal;
    private Course course;
    private Unbinder unbinder;
    private SimpleDateFormat sqlFormat;
    private CourseHandler handler;
    private ArrayList<Day> days;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_course_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle(getString(R.string.course_edit));

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        sqlFormat = new SimpleDateFormat(Utils.SQL_DATETIME_FORMAT);
        cal = new GregorianCalendar();
        handler = new CourseHandler(getContext());


        if(savedInstanceState == null)
            course = getArguments().getParcelable(BundleUtils.BUNDLE_COURSE);
        else
            course = savedInstanceState.getParcelable(BundleUtils.BUNDLE_COURSE);

        //Get class days
        days = handler.getClassDaysByCourseID(course.getCourseID());
        course.setCourseStartDate(handler.getFirstClassDateByCourse(course.getCourseID()));
        course.setCourseEndDate(handler.getLastClassDateByCourse(course.getCourseID()));


        Log.e("Professor ID", String.valueOf(course.getProfID()));

        //Add Course Information
        courseLocation.setText(course.getLocation());
        courseName.setText(course.getCourseName());
        courseDays.setText(handler.getCourseDays(days));
        profName.setText(course.getProfName());
        courseColor.setColorFilter(course.getCourseColor());
        collapsingToolbar.setContentScrimColor(course.getCourseColor());
        collapsingToolbar.setBackgroundColor(course.getCourseColor());


        //Add Course Times
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.COURSE_TIME);

            if(course.getCourseStart() != null) {
                long startTime = sqlFormat.parse(course.getCourseStart()).getTime();
                courseStart.setText(dateFormat.format(startTime));
            }

            if(course.getCourseEnd() != null) {
                long endTime = sqlFormat.parse(course.getCourseEnd()).getTime();
                courseEnd.setText(dateFormat.format(endTime));
            }


            dateFormat = new SimpleDateFormat(Utils.DISPLAY_DATE);

            if(course.getCourseStartDate() != null) {
                long startDate = sqlFormat.parse(course.getCourseStartDate()).getTime();
                courseStartDate.setText(dateFormat.format(startDate));
            }


            if(course.getCourseEndDate() != null) {
                long endDate = sqlFormat.parse(course.getCourseEndDate()).getTime();
                courseEndDate.setText(dateFormat.format(endDate));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        course.setCourseName(courseName.getText().toString());
        course.setLocation(courseLocation.getText().toString());

        outState.putParcelable(BundleUtils.BUNDLE_COURSE, course);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fab, R.id.courseColorRow, R.id.courseStartRow, R.id.courseEndRow, R.id.professorRow, R.id.courseDaysRow,
            R.id.courseStartDateRow, R.id.courseEndDateRow})
    @Override
    public void onClick(View v) {
        DialogFragment fragment;

        switch (v.getId())
        {
            case R.id.fab:
                updateCourse();
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

            case R.id.courseStartDateRow:
                timeType = Utils.COURSE_DATE_FROM;
                fragment = new DatePickerDialog();
                fragment.setTargetFragment(this, 0);
                fragment.show(getActivity().getSupportFragmentManager(), "START");
                break;

            case R.id.courseEndDateRow:
                dateType = Utils.COURSE_DATE_TO;
                fragment = new DatePickerDialog();
                fragment.setTargetFragment(this, 0);
                fragment.show(getActivity().getSupportFragmentManager(), "END");
                break;


            case R.id.professorRow:
                fragment = new AddProfessorDialog();
                fragment.setTargetFragment(this, 0);
                fragment.show(getActivity().getSupportFragmentManager(), "PROFESSOR");
                break;

            case R.id.courseDaysRow:
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(BundleUtils.BUNDLE_CLASS_DATES, days);
                fragment = new CourseDaysDialog();
                fragment.setArguments(bundle);
                fragment.setTargetFragment(this, 0);
                fragment.show(getActivity().getSupportFragmentManager(), "DAYS");
                break;

            case R.id.courseColorRow:
                colorDialog(course.getCourseColor());
                break;
        }
    }

    @Override
    public void OnClassDaysSelected(ArrayList<Day> days) {
        this.days = days;
        courseDays.setText(handler.getCourseDays(days));
    }

    @Override
    public void onTimeSelected(int hourOfDay, int minute) {
        cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.COURSE_TIME);


        if(timeType == Utils.COURSE_TIME_FROM)
        {
            long startTime = cal.getTimeInMillis();
            courseStart.setText(dateFormat.format(startTime));
            course.setCourseStart(new SimpleDateFormat(Utils.SQL_DATE_FORMAT).format(startTime));
        }
        else
        {
            long endTime = cal.getTimeInMillis();
            courseEnd.setText(dateFormat.format(endTime));
            course.setCourseEnd(new SimpleDateFormat(Utils.SQL_DATE_FORMAT).format(endTime));
        }
    }


    @Override
    public void onDatePick(int day, int month, int year) {
        SimpleDateFormat f = new SimpleDateFormat(Utils.DISPLAY_DATE);
        if(dateType == Utils.COURSE_DATE_FROM)
        {
            //Sets the Start Date
            cal.set(year, month, day);
            cal.set(Calendar.HOUR_OF_DAY, 11);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            courseStartDate.setText(f.format(cal.getTimeInMillis()));
            course.setCourseStartDate(sqlFormat.format(cal.getTimeInMillis()));
        }
        else {
            cal.set(year, month, day);
            cal.set(Calendar.HOUR_OF_DAY, 11);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            courseEndDate.setText(f.format(cal.getTimeInMillis()));
            course.setCourseEndDate(sqlFormat.format(cal.getTimeInMillis()));
        }
    }

    @Override
    public void onItemClicked(Professor item) {
        course.setProfID(item.getId());
        course.setProfName(item.getName());

        profName.setText(course.getProfName());
    }

    private void updateCourse()
    {
        if(courseName.getText().toString().isEmpty())
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_course_name), Snackbar.LENGTH_LONG).show();

        else if(profName.getText().toString().isEmpty())
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_course_professor), Snackbar.LENGTH_LONG).show();

        else if (course.getCourseStartDate() != null && course.getCourseEndDate() == null)
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_course_end_date), Snackbar.LENGTH_LONG).show();

        else if(course.getCourseStartDate() == null && course.getCourseEndDate() != null)
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_course_start_date), Snackbar.LENGTH_LONG).show();

        else if(course.getCourseStart() != null && course.getCourseEnd() == null)
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_course_end_time), Snackbar.LENGTH_LONG).show();

        else if(course.getCourseStart() == null && course.getCourseEnd() != null)
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_course_start_time), Snackbar.LENGTH_LONG).show();

        else if (course.getCourseStartDate() != null && course.getCourseEndDate() != null && course.getCourseStart() == null)
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_course_start_time), Snackbar.LENGTH_LONG).show();

        else if (course.getCourseStartDate() != null && course.getCourseEndDate() != null && course.getCourseEnd() == null)
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_course_end_time), Snackbar.LENGTH_LONG).show();

        else
        {
            course.setCourseName(courseName.getText().toString());
            course.setLocation(courseLocation.getText().toString());

            //Update Course
            handler.updateCourse(course);

            //Add class days and dates
            handler.updateCourseDays(days);

            if(course.getCourseStartDate() != null &&  course.getCourseEndDate() != null)
                addClassDates();

            getActivity().finish();
        }
    }


    private void addClassDates()
    {
        //Class Days Intent Service
        ArrayList<Integer> classDays = new ArrayList<>();

        for(Day d: days)
        {
            if(d.getDayChecked() == Utils.CHECKED)
                classDays.add(d.getDayValue());
        }

        Intent intent = new Intent(getContext(), ClassDatesIntentService.class);
        intent.setAction(ServiceUtils.SERVICE_ADD_CLASS_DATES);
        intent.putExtra(BundleUtils.BUNDLE_CLASS_DATES, classDays);
        intent.putExtra(BundleUtils.BUNDLE_COURSE, course);
        getActivity().startService(intent);
    }


    private void colorDialog(int color)
    {
        //int[] mColors = getResources().getIntArray(R.array.default_rainbow);
        int[] mColors = new int[]{ContextCompat.getColor(getContext(),R.color.colorPrimary),ContextCompat.getColor(getContext(),R.color.colorAccent),
                ContextCompat.getColor(getContext(),R.color.grape), ContextCompat.getColor(getContext(),R.color.blueberry),
                ContextCompat.getColor(getContext(),R.color.blue), ContextCompat.getColor(getContext(),R.color.blue_light),
                ContextCompat.getColor(getContext(),R.color.peacock), ContextCompat.getColor(getContext(),R.color.sage),
                ContextCompat.getColor(getContext(),R.color.green), ContextCompat.getColor(getContext(),R.color.yellow),
                ContextCompat.getColor(getContext(),R.color.orange),ContextCompat.getColor(getContext(),R.color.tangerine),
                ContextCompat.getColor(getContext(),R.color.azure), ContextCompat.getColor(getContext(),R.color.graphite),
                ContextCompat.getColor(getContext(),R.color.tomato), ContextCompat.getColor(getContext(),R.color.basil)};

        ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.course_color,
                mColors,
                color,
                4, // Number of columns
                ColorPickerDialog.SIZE_SMALL);


        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                course.setCourseColor(color);
                courseColor.setColorFilter(color);
            }

        });

        dialog.show(getActivity().getFragmentManager(),"test");
    }
}
