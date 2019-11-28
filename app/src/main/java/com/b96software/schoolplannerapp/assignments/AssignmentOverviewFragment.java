package com.b96software.schoolplannerapp.assignments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.handlers.AssignmentHandler;
import com.b96software.schoolplannerapp.model.Assignment;
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

public class AssignmentOverviewFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1000005;

    @BindView(R.id.assignType)
    TextView assignType;

    @BindView(R.id.assignDueDate)
    TextView assignDueDate;

    @BindView(R.id.courseName)
    TextView assignCourse;

    @BindView(R.id.assignGrade)
    TextView assignGrade;

    @BindView(R.id.assignProgress)
    TextView assignProgress;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    //Objects/Variables
    private Calendar cal;
    private Assignment assignment;
    private Unbinder unbinder;
    private SimpleDateFormat format;
    private AssignmentHandler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.overview_assignment_fragment, container, false);
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

        cal = new GregorianCalendar();
        handler = new AssignmentHandler(getContext());
        format = new SimpleDateFormat(Utils.DISPLAY_DATE);


        if(savedInstanceState == null)
            assignment = getArguments().getParcelable(BundleUtils.BUNDLE_ASSIGNMENT);
        else
            assignment = savedInstanceState.getParcelable(BundleUtils.BUNDLE_ASSIGNMENT);


        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BundleUtils.BUNDLE_ASSIGNMENT, assignment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return handler.getAssignment(assignment.getId());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        assignment = handler.getAssignmentFromCursor(cursor);

        if(assignment != null) {

            //Toolbar Info
            collapsingToolbar.setTitle(assignment.getName());
            collapsingToolbar.setBackgroundColor(assignment.getCourseColor());
            collapsingToolbar.setContentScrimColor(assignment.getCourseColor());
            toolbar.setBackgroundColor(assignment.getCourseColor());

            if(getActivity().findViewById(R.id.toolbar) != null)
                ((Toolbar)getActivity().findViewById(R.id.toolbar)).setBackgroundColor(assignment.getCourseColor());


            assignCourse.setText(assignment.getCourseName());
            assignGrade.setText(assignment.getGrade() == null ? "0" : assignment.getGrade());

            assignProgress.setText(assignment.getAssignProgress() == Utils.ASSIGN_TYPE_COMPLETED ?
                    getString(R.string.assignment_completed) : getString(R.string.assignment_not_done));

            assignType.setText(assignment.getAssignType() == Utils.ASSIGN_TYPE_HOMEWORK ?
                    getString(R.string.assignment_homework) : getString(R.string.assignment_exam));

            try {
                long date = new SimpleDateFormat(Utils.SQL_DATE_FORMAT).parse(assignment.getDate()).getTime();
                cal.setTimeInMillis(date);
                assignDueDate.setText(format.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @OnClick(R.id.fab)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fab:
                editAssignment();
                break;
        }
    }

    private void editAssignment()
    {
        Intent intent = new Intent(getContext(), EditAssignmentActivity.class);
        intent.putExtra(BundleUtils.BUNDLE_ASSIGNMENT, assignment);
        startActivity(intent);
    }
}
