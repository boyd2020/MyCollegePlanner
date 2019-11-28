package com.b96software.schoolplannerapp.assignments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.dialog.AddCourseDialog;
import com.b96software.schoolplannerapp.dialog.AddGradeDialog;
import com.b96software.schoolplannerapp.dialog.DatePickerDialog;
import com.b96software.schoolplannerapp.dialog.TimePickerDialog;
import com.b96software.schoolplannerapp.handlers.AssignmentHandler;
import com.b96software.schoolplannerapp.handlers.GradeHandler;
import com.b96software.schoolplannerapp.interfaces.GradeAddedListener;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Assignment;
import com.b96software.schoolplannerapp.model.Course;
import com.b96software.schoolplannerapp.model.Grade;
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

public class AddAssignmentFragment extends Fragment implements View.OnClickListener,
        DatePickerDialog.OnDatePickListener, TimePickerDialog.OnTimeSelectedListener, ItemClickedListener<Course>,
        GradeAddedListener
{


    @BindView(R.id.assignName)
    EditText assignmentName;

    @BindView(R.id.assignCourse)
    TextView assignCourse;

    @BindView(R.id.assignDueDate)
    TextView assignDueDate;

    @BindView(R.id.assignGrade)
    TextView assignGrade;

    @BindView(R.id.radio_progress_group)
    RadioGroup assignProgressGroup;

    @BindView(R.id.radio_assign_group)
    RadioGroup assignTypeGroup;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    //Variable
    private Course course;
    private Grade grade;
    private Calendar cal;
    private Assignment assignment;
    private Unbinder unbinder;
    private SimpleDateFormat format;
    private AssignmentHandler handler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_assignment_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle(getString(R.string.assignment_add));

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        cal = new GregorianCalendar();
        handler = new AssignmentHandler(getContext());
        format = new SimpleDateFormat(Utils.DISPLAY_DATE);

        if(savedInstanceState == null)
        {
            assignment = new Assignment();
            course = new Course();
            grade = new Grade();

            assignment.setDate(new SimpleDateFormat(Utils.SQL_DATE_FORMAT).format(cal.getTime()));
            assignDueDate.setText(format.format(cal.getTimeInMillis()));
        }
        else
        {
            assignment = savedInstanceState.getParcelable(BundleUtils.BUNDLE_ASSIGNMENT);
            grade = savedInstanceState.getParcelable(BundleUtils.BUNDLE_GRADE);
            course = savedInstanceState.getParcelable(BundleUtils.BUNDLE_COURSE);

            assignmentName.setText(assignment.getName());
            assignCourse.setText(course.getCourseName());
            assignGrade.setText(String.valueOf(grade.getEarned()));

            try {
                long date = new SimpleDateFormat(Utils.SQL_DATE_FORMAT).parse(assignment.getDate()).getTime();
                cal.setTimeInMillis(date);
                assignDueDate.setText(format.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        assignment.setName(assignmentName.getText().toString());
        assignment.setAssignType(assignProgressGroup.getCheckedRadioButtonId() == R.id.radio_exam ? Utils.ASSIGN_TYPE_EXAM : Utils.ASSIGN_TYPE_HOMEWORK);
        assignment.setAssignProgress(assignProgressGroup.getCheckedRadioButtonId() == R.id.radio_not_done ? Utils.ASSIGN_TYPE_NOT_DONE : Utils.ASSIGN_TYPE_COMPLETED);

        outState.putParcelable(BundleUtils.BUNDLE_GRADE, grade);
        outState.putParcelable(BundleUtils.BUNDLE_COURSE, course);
        outState.putParcelable(BundleUtils.BUNDLE_ASSIGNMENT, assignment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.assignCourseRow, R.id.assignDateRow, R.id.gradeRow, R.id.fab})
    @Override
    public void onClick(View v) {

        DialogFragment fragment;
        FragmentManager fm = getActivity().getSupportFragmentManager();

        switch (v.getId())
        {
            case R.id.assignCourseRow:
                fragment = new AddCourseDialog();
                fragment.setTargetFragment(this, 0);
                fragment.show(fm, "COURSE");
                break;

            case R.id.assignDateRow:
                fragment = new DatePickerDialog();
                fragment.setTargetFragment(this, 0);
                fragment.show(fm, "DATE");
                break;

            case R.id.gradeRow:
                Bundle bundle = new Bundle();
                bundle.putParcelable(BundleUtils.BUNDLE_GRADE, grade);

                fragment = new AddGradeDialog();
                fragment.setArguments(bundle);
                fragment.setTargetFragment(this, 0);
                fragment.show(fm, "GRADE");
                break;

            case R.id.fab:
                addAssignment();
                break;
        }
    }

    @Override
    public void onDatePick(int day, int month, int year) {
        cal.set(year, month, day);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        assignment.setDate(new SimpleDateFormat(Utils.SQL_DATE_FORMAT).format(cal.getTimeInMillis()));

        //Starts Time Dialog
        DialogFragment fragment = new TimePickerDialog();
        fragment.setTargetFragment(this, 0);
        fragment.show(getActivity().getSupportFragmentManager(), "TIME");
    }

    @Override
    public void onTimeSelected(int hourOfDay, int minute) {
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);

        assignment.setDate(new SimpleDateFormat(Utils.SQL_DATE_FORMAT).format(cal.getTimeInMillis()));
        assignDueDate.setText(format.format(cal.getTimeInMillis()));
    }


    @Override
    public void onItemClicked(Course course) {
        this.course = course;
        assignment.setCourseID(course.getCourseID());
        assignCourse.setText(course.getCourseName());
    }

    @Override
    public void onGradeAdded(Grade grade) {
        this.grade = grade;
        assignGrade.setText(String.valueOf(grade.getEarned()));
    }

    private void addAssignment()
    {
        if(assignmentName.getText().toString().isEmpty())
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_assign_name), Snackbar.LENGTH_LONG).show();

        else if (assignDueDate.getText().toString().isEmpty())
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_assign_date), Snackbar.LENGTH_LONG).show();

        else if(course.getCourseID() == 0)
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_assign_course), Snackbar.LENGTH_LONG).show();

        else
        {
            String name = assignmentName.getText().toString();
            int assignType = Utils.ASSIGN_TYPE_HOMEWORK;
            int assignProgress = Utils.ASSIGN_TYPE_NOT_DONE;


            if (assignTypeGroup.getCheckedRadioButtonId() == R.id.radio_exam)
                assignType = Utils.ASSIGN_TYPE_EXAM;

            if (assignProgressGroup.getCheckedRadioButtonId() == R.id.radio_completed)
                assignProgress = Utils.ASSIGN_TYPE_COMPLETED;


            assignment.setAssignProgress(assignProgress);
            assignment.setAssignType(assignType);
            assignment.setName(name);


            int assignID = handler.addAssignment(assignment);

            //Add Grade
            grade.setAssignID(assignID);
            grade.setDate(assignment.getDate());
            new GradeHandler(getContext()).addGrade(grade);

            getActivity().finish();

        }
    }

}
