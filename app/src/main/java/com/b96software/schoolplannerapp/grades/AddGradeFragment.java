package com.b96software.schoolplannerapp.grades;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.dialog.AddAssignmentDialog;
import com.b96software.schoolplannerapp.dialog.DatePickerDialog;
import com.b96software.schoolplannerapp.handlers.GradeHandler;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Assignment;
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

public class AddGradeFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDatePickListener,
        ItemClickedListener<Assignment> {

    @BindView(R.id.gradeAssignment)
    TextView gradeAssignment;

    @BindView(R.id.gradeEarned)
    EditText gradeEarned;

    @BindView(R.id.gradeWorth)
    EditText gradeWorth;

    @BindView(R.id.gradeDate)
    TextView gradeDate;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;
    private Calendar cal;
    private Grade grade;
    private Assignment assignment;
    private GradeHandler handler;
    private SimpleDateFormat format;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_grade_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle(getString(R.string.grade_edit));

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        cal = new GregorianCalendar();
        handler = new GradeHandler(getContext());
        format = new SimpleDateFormat(Utils.DISPLAY_DATE);

        if(savedInstanceState == null)
        {
            grade = new Grade();
            assignment = new Assignment();
        }
        else
        {
            grade = savedInstanceState.getParcelable(BundleUtils.BUNDLE_GRADE);
            assignment = savedInstanceState.getParcelable(BundleUtils.BUNDLE_ASSIGNMENT);

            gradeWorth.setText(String.valueOf(grade.getWorth()));
            gradeEarned.setText(String.valueOf(grade.getEarned()));

            try {
                long date = new SimpleDateFormat(Utils.SQL_DATE_FORMAT).parse(grade.getDate()).getTime();
                cal.setTimeInMillis(date);
                gradeDate.setText(format.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }



        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BundleUtils.BUNDLE_GRADE, grade);
        outState.putParcelable(BundleUtils.BUNDLE_ASSIGNMENT, assignment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.gradeDateRow, R.id.gradeAssignmentRow, R.id.fab})
    @Override
    public void onClick(View v) {
        DialogFragment fragment;

        switch (v.getId())
        {
            case R.id.gradeDateRow:
                fragment = new DatePickerDialog();
                fragment.setTargetFragment(this, 0);
                fragment.show(getActivity().getSupportFragmentManager(), "Date");
                break;

            case R.id.gradeAssignmentRow:
                fragment = new AddAssignmentDialog();
                fragment.setTargetFragment(this, 0);
                fragment.show(getActivity().getSupportFragmentManager(), "Assignment");
                break;

            case R.id.fab:
                addGrade();
                break;
        }
    }

    @Override
    public void onDatePick(int day, int month, int year) {
        cal.set(year, month, day);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        grade.setDate(new SimpleDateFormat(Utils.SQL_DATE_FORMAT).format(cal.getTimeInMillis()));
        gradeDate.setText(format.format(cal.getTimeInMillis()));
    }

    @Override
    public void onItemClicked(Assignment assignment) {
        this.assignment = assignment;

        grade.setAssignID(assignment.getId());
        grade.setAssignmentName(assignment.getName());

        gradeAssignment.setText(assignment.getName());
    }

    private void addGrade()
    {
        if(gradeEarned.getText().toString().isEmpty())
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_grade_earned), Snackbar.LENGTH_LONG).show();

        else if(gradeWorth.getText().toString().isEmpty())
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_grade_worth), Snackbar.LENGTH_LONG).show();

        else if (gradeAssignment.getText().toString().isEmpty())
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_grade_assignment), Snackbar.LENGTH_LONG).show();
        else
        {
            double pointsEarned = Double.parseDouble(gradeEarned.getText().toString());
            double pointsWorth = Double.parseDouble(gradeWorth.getText().toString());

            grade.setEarned(pointsEarned);
            grade.setWorth(pointsWorth);

            int update = handler.updateAssignmentGrade(grade);

            if(update == 0)
                handler.addGrade(grade);

            getActivity().finish();
        }
    }
}
