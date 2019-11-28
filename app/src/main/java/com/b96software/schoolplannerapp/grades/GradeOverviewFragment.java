package com.b96software.schoolplannerapp.grades;

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
import com.b96software.schoolplannerapp.handlers.GradeHandler;
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

public class GradeOverviewFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1000004;


    @BindView(R.id.gradeDate)
    TextView gradeDate;

    @BindView(R.id.gradeEarned)
    TextView gradeEarned;

    @BindView(R.id.gradeWorth)
    TextView gradeWorth;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    //Variables/Object
    private Calendar cal;
    private Unbinder unbinder;
    private SimpleDateFormat format;
    private GradeHandler handler;
    private Grade grade;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.overview_grade_fragment, container, false);

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
        handler = new GradeHandler(getContext());
        format = new SimpleDateFormat(Utils.DISPLAY_DATE);

        if(savedInstanceState == null)
            grade = getArguments().getParcelable(BundleUtils.BUNDLE_GRADE);
        else
            grade = savedInstanceState.getParcelable(BundleUtils.BUNDLE_GRADE);


        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);


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
        outState.putParcelable(BundleUtils.BUNDLE_GRADE, grade);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return handler.getGradeFromDB(grade.getId());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        grade = handler.getGradeFromCursor(cursor);

        if(grade != null) {

            //Toolbar Info
            collapsingToolbar.setTitle(grade.getAssignmentName());
            collapsingToolbar.setBackgroundColor(grade.getCourseColor());
            collapsingToolbar.setContentScrimColor(grade.getCourseColor());
            toolbar.setBackgroundColor(grade.getCourseColor());

            if(getActivity().findViewById(R.id.toolbar) != null)
                ((Toolbar)getActivity().findViewById(R.id.toolbar)).setBackgroundColor(grade.getCourseColor());


            gradeEarned.setText(String.valueOf(grade.getEarned()));
            gradeWorth.setText(String.valueOf(grade.getWorth()));

            try {
                if(grade.getDate() != null) {
                    long date = new SimpleDateFormat(Utils.SQL_DATE_FORMAT).parse(grade.getDate()).getTime();
                    cal.setTimeInMillis(date);
                    gradeDate.setText(format.format(date));
                }
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
                editGrade();
                break;
        }
    }

    private void editGrade()
    {
        Intent intent = new Intent(getContext(), EditGradeActivity.class);
        intent.putExtra(BundleUtils.BUNDLE_GRADE, grade);
        startActivity(intent);
    }
}
